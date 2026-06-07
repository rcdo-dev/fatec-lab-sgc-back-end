package br.com.sgc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.burial.dto.request.BurialRequestDTO;
import br.com.sgc.api.burial.service.BurialService;
import br.com.sgc.api.cemetery.entity.BlockEntity;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.entity.GraveEntity;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.BurialStatus;
import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.enums.GenderIdentityType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.enums.GraveType;
import br.com.sgc.api.common.enums.HairType;
import br.com.sgc.api.common.enums.SkinColor;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.person.dto.request.DeathRequestDTO;
import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.DeceasedPetRequestDTO;
import br.com.sgc.api.person.dto.request.DeceasedUnidentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.request.support.AddressInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.ContactInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;
import br.com.sgc.api.person.repositories.DeceasedIdentifiedRepository;
import br.com.sgc.api.person.service.DeathService;
import br.com.sgc.api.person.service.DeceasedIdentifiedService;
import br.com.sgc.api.person.service.DeceasedPetService;
import br.com.sgc.api.person.service.DeceasedUnidentifiedService;
import br.com.sgc.api.person.service.DeclarantService;

@SpringBootTest
@Transactional
class LifecycleServiceIntegrationTests {

    @Autowired
    private DeclarantService declarantService;

    @Autowired
    private DeceasedIdentifiedService identifiedService;

    @Autowired
    private DeceasedUnidentifiedService unidentifiedService;

    @Autowired
    private DeceasedPetService petService;

    @Autowired
    private DeathService deathService;

    @Autowired
    private BurialService burialService;

    @Autowired
    private CemeteryRepository cemeteryRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private GraveRepository graveRepository;

    @Autowired
    private DeceasedIdentifiedRepository identifiedRepository;

    private long sequence;

    @Test
    void createsDeceasedWithSystemControlledDefaultsAndMapsLifecycleFields() {
        var declarantId = createDeclarant();
        var response = identifiedService.save(identifiedRequest(declarantId, GenderIdentityType.CISGENDER));

        assertEquals(DeceasedStatus.ACTIVE, response.deceasedStatus());
        assertFalse(response.archived());
        assertNull(response.archivedAt());
    }

    @Test
    void fixesPetUpdateAndIdentifiedGenderIdentityUpdate() {
        var declarantId = createDeclarant();
        var pet = petService.save(petRequest(declarantId, "Rex"));
        var updatedPet = petService.update(pet.id(), petRequest(declarantId, "Mel"));

        assertEquals("Mel", updatedPet.name());

        var identified = identifiedService.save(identifiedRequest(declarantId, GenderIdentityType.CISGENDER));
        var updatedIdentified = identifiedService.update(
                identified.id(),
                identifiedRequest(declarantId, GenderIdentityType.TRANSGENDER));

        assertEquals(GenderIdentityType.TRANSGENDER, updatedIdentified.genderIdentity());
    }

    @Test
    void blocksArchiveWhenDeceasedHasActiveBurial() {
        var deceasedId = createIdentifiedWithDeath();
        var grave = createGrave(2, false, true, GraveStatus.AVAILABLE);

        burialService.save(burialRequest(deceasedId, grave.getId()));

        assertThrows(BusinessException.class, () -> identifiedService.archive(deceasedId));
    }

    @Test
    void blocksMutationsForArchivedDeceased() {
        var declarantId = createDeclarant();
        var identified = identifiedService.save(identifiedRequest(declarantId, GenderIdentityType.CISGENDER));

        identifiedService.archive(identified.id());

        assertThrows(BusinessException.class,
                () -> identifiedService.update(identified.id(), identifiedRequest(declarantId, GenderIdentityType.CISGENDER)));
        assertThrows(BusinessException.class, () -> deathService.save(deathRequest(identified.id(), "Casa")));
    }

    @Test
    void keepsOneDeathPerDeceasedAndPreservesAssociationOnUpdate() {
        var deceasedId = createIdentified();
        var death = deathService.save(deathRequest(deceasedId, "Casa"));

        var updated = deathService.update(death.id(), deathRequest(deceasedId, "Hospital"));

        assertEquals("Hospital", updated.place());
        assertEquals(deceasedId, updated.deceasedId());
        assertThrows(ConflictException.class, () -> deathService.save(deathRequest(deceasedId, "Outro local")));

        var otherDeceasedId = createIdentified();
        assertThrows(BusinessException.class, () -> deathService.update(death.id(), deathRequest(otherDeceasedId, "IML")));
    }

    @Test
    void requiresDeathBeforeBurial() {
        var deceasedId = createIdentified();
        var grave = createGrave(2, false, true, GraveStatus.AVAILABLE);

        assertThrows(BusinessException.class, () -> burialService.save(burialRequest(deceasedId, grave.getId())));
    }

    @Test
    void enforcesGraveCapacityByActiveBurials() {
        var grave = createGrave(2, false, true, GraveStatus.AVAILABLE);
        var firstDeceasedId = createIdentifiedWithDeath();
        var secondDeceasedId = createIdentifiedWithDeath();
        var thirdDeceasedId = createIdentifiedWithDeath();

        burialService.save(burialRequest(firstDeceasedId, grave.getId()));
        burialService.save(burialRequest(secondDeceasedId, grave.getId()));

        var persistedGrave = graveRepository.findById(grave.getId()).orElseThrow();
        assertEquals(GraveStatus.OCCUPIED, persistedGrave.getStatus());
        assertThrows(BusinessException.class, () -> burialService.save(burialRequest(thirdDeceasedId, grave.getId())));
    }

    @Test
    void cancellationReleasesCapacityAndSynchronizesGraveStatus() {
        var grave = createGrave(2, false, true, GraveStatus.AVAILABLE);
        var firstDeceasedId = createIdentifiedWithDeath();
        var secondDeceasedId = createIdentifiedWithDeath();

        var firstBurial = burialService.save(burialRequest(firstDeceasedId, grave.getId()));
        var secondBurial = burialService.save(burialRequest(secondDeceasedId, grave.getId()));

        burialService.cancel(firstBurial.id());
        assertEquals(GraveStatus.OCCUPIED, graveRepository.findById(grave.getId()).orElseThrow().getStatus());
        assertEquals(DeceasedStatus.ACTIVE, identifiedRepository.findById(firstDeceasedId).orElseThrow().getStatus());

        burialService.cancel(secondBurial.id());
        assertEquals(GraveStatus.AVAILABLE, graveRepository.findById(grave.getId()).orElseThrow().getStatus());
        assertEquals(DeceasedStatus.ACTIVE, identifiedRepository.findById(secondDeceasedId).orElseThrow().getStatus());
        assertEquals(BurialStatus.CANCELLED, burialService.findById(secondBurial.id()).status());
    }

    @Test
    void rejectsBlockedInactiveAndMaintenanceGraves() {
        assertRejectedGrave(createGrave(1, true, true, GraveStatus.AVAILABLE));
        assertRejectedGrave(createGrave(1, false, false, GraveStatus.AVAILABLE));
        assertRejectedGrave(createGrave(1, false, true, GraveStatus.MAINTENANCE));
    }

    @Test
    void protectsDeclarantDeleteWhenRelatedDeceasedExists() {
        var declarantId = createDeclarant();
        identifiedService.save(identifiedRequest(declarantId, GenderIdentityType.CISGENDER));

        assertThrows(BusinessException.class, () -> declarantService.delete(declarantId));
    }

    private void assertRejectedGrave(GraveEntity grave) {
        var deceasedId = createIdentifiedWithDeath();
        assertThrows(BusinessException.class, () -> burialService.save(burialRequest(deceasedId, grave.getId())));
    }

    private Long createIdentifiedWithDeath() {
        var deceasedId = createIdentified();
        deathService.save(deathRequest(deceasedId, "Casa"));
        return deceasedId;
    }

    private Long createIdentified() {
        var declarantId = createDeclarant();
        return identifiedService.save(identifiedRequest(declarantId, GenderIdentityType.CISGENDER)).id();
    }

    private Long createDeclarant() {
        return declarantService.save(declarantRequest()).id();
    }

    private GraveEntity createGrave(int capacity, boolean blocked, boolean active, GraveStatus status) {
        var number = nextInt();

        var cemetery = new CemeteryEntity();
        cemetery.setName("Cemiterio " + number);
        cemetery.setFoundation(LocalDate.of(1980, 1, 1));
        cemetery.setActive(true);
        cemetery = cemeteryRepository.save(cemetery);

        var block = new BlockEntity();
        block.setNumber(number);
        block.setDescription("Quadra " + number);
        block.setActive(true);
        block.setCemetery(cemetery);
        block = blockRepository.save(block);

        var grave = new GraveEntity();
        grave.setNumber(number);
        grave.setGraveType(capacity > 2 ? GraveType.MAUSOLEUM : GraveType.EARTH);
        grave.setBodyCapacity(capacity);
        grave.setAreaType(AreaType.PERPETUAL);
        grave.setActive(active);
        grave.setStatus(status);
        grave.setBlocked(blocked);
        grave.setBlock(block);
        return graveRepository.save(grave);
    }

    private DeclarantRequestDTO declarantRequest() {
        var number = nextInt();
        return new DeclarantRequestDTO(
                "Declarante " + number,
                new DocumentInfoRequestDTO("RGD" + number, cpf(10000000000L + number)),
                new ContactInfoRequestDTO("1199999" + number, "contato" + number + "@email.com"),
                new AddressInfoRequestDTO("Rua " + number, "10", "Centro", "Santos", "SP", "11000000"),
                "Analista");
    }

    private DeceasedIdentifiedRequestDTO identifiedRequest(Long declarantId, GenderIdentityType genderIdentity) {
        var number = nextInt();
        return new DeceasedIdentifiedRequestDTO(
                "Falecido " + number,
                LocalDate.of(1970, 1, 1),
                GenderType.MALE,
                genderIdentity,
                new DocumentInfoRequestDTO("RGF" + number, cpf(20000000000L + number)),
                "Ocupacao",
                "Pai",
                "Mae",
                "Santos",
                true,
                "Observacao",
                DeceasedStatus.ARCHIVED,
                true,
                LocalDateTime.now(),
                declarantId);
    }

    private DeceasedPetRequestDTO petRequest(Long declarantId, String name) {
        return new DeceasedPetRequestDTO(
                name,
                "Canino",
                "Sem raca definida",
                GenderType.MALE,
                "Caramelo",
                8,
                DeceasedStatus.ARCHIVED,
                true,
                LocalDateTime.now(),
                declarantId);
    }

    @SuppressWarnings("unused")
    private DeceasedUnidentifiedRequestDTO unidentifiedRequest(Long declarantId) {
        return new DeceasedUnidentifiedRequestDTO(
                45,
                GenderType.MALE,
                SkinColor.BROWN,
                1.75f,
                "Preto",
                HairType.STRAIGHT,
                "Castanho",
                br.com.sgc.api.common.enums.EyeType.ROUND,
                DeceasedStatus.ARCHIVED,
                true,
                LocalDateTime.now(),
                declarantId);
    }

    private DeathRequestDTO deathRequest(Long deceasedId, String place) {
        return new DeathRequestDTO(
                place,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 30),
                "Causa natural",
                "Dr. Responsavel",
                "CERT-" + nextInt(),
                "Observacao",
                deceasedId);
    }

    private BurialRequestDTO burialRequest(Long deceasedId, Long graveId) {
        return new BurialRequestDTO(
                LocalDate.of(2025, 1, 2),
                LocalTime.of(11, 0),
                "Sepultamento conforme protocolo",
                deceasedId,
                graveId);
    }

    private int nextInt() {
        return (int) ++sequence;
    }

    private String cpf(long value) {
        return String.format("%011d", value % 100000000000L);
    }
}
