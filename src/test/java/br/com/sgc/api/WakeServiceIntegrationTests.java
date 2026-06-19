package br.com.sgc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.burial.dto.request.WakeRequestDTO;
import br.com.sgc.api.burial.service.WakeService;
import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.entity.WakeConfigurationEntity;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.cemetery.repositories.WakeConfigurationRepository;
import br.com.sgc.api.cemetery.service.CemeteryService;
import br.com.sgc.api.common.enums.GenderIdentityType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.common.enums.WakeStatus;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.request.support.AddressInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.ContactInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;
import br.com.sgc.api.person.service.DeceasedIdentifiedService;
import br.com.sgc.api.person.service.DeclarantService;

@SpringBootTest
@Transactional
class WakeServiceIntegrationTests {

    @Autowired
    private WakeService wakeService;

    @Autowired
    private DeclarantService declarantService;

    @Autowired
    private DeceasedIdentifiedService identifiedService;

    @Autowired
    private CemeteryRepository cemeteryRepository;

    @Autowired
    private WakeConfigurationRepository wakeConfigurationRepository;

    @Autowired
    private CemeteryService cemeteryService;

    private long sequence;

    @Test
    void createsWakeWithSingleActiveCemeteryWithoutCemeteryId() {
        var deceasedId = createIdentified();
        var cemetery = createCemetery();

        var response = wakeService.save(wakeRequest(deceasedId, null, LocalTime.of(10, 0), LocalTime.of(13, 0)));

        assertEquals(WakeStatus.SCHEDULED, response.status());
        assertEquals(cemetery.getId(), response.cemeteryId());
        assertEquals(BigDecimal.ZERO, response.appliedFee());
        assertEquals("Velório registrado com sucesso.", response.message());
    }

    @Test
    void cemeteryCreationCreatesDefaultWakeConfiguration() {
        var cemetery = cemeteryService.save(new CemeteryRequestDTO(
                "Cemiterio Config " + nextInt(),
                LocalDate.of(1980, 1, 1),
                true));

        var configuration = wakeConfigurationRepository.findByCemeteryId(cemetery.id()).orElseThrow();

        assertEquals(180, configuration.getDurationMinutes());
        assertEquals(false, configuration.isCharged());
        assertEquals(BigDecimal.ZERO, configuration.getFee());
    }

    @Test
    void cemeteryUpdateChangesWakeConfiguration() {
        var cemetery = cemeteryService.save(new CemeteryRequestDTO(
                "Cemiterio Config " + nextInt(),
                LocalDate.of(1980, 1, 1),
                true));

        cemeteryService.update(cemetery.id(), new CemeteryRequestDTO(
                cemetery.name(),
                cemetery.foundation(),
                true,
                120,
                true,
                new BigDecimal("200.00")));

        var configuration = wakeConfigurationRepository.findByCemeteryId(cemetery.id()).orElseThrow();

        assertEquals(120, configuration.getDurationMinutes());
        assertEquals(true, configuration.isCharged());
        assertEquals(new BigDecimal("200.00"), configuration.getFee());
    }

    @Test
    void createsWakeWithCemeteryIdAndPersistsAppliedFeeSnapshot() {
        var deceasedId = createIdentified();
        var cemetery = createCemetery();
        configureWake(cemetery, 180, true, new BigDecimal("150.00"));

        var response = wakeService.save(
                wakeRequest(deceasedId, cemetery.getId(), LocalTime.of(10, 0), LocalTime.of(13, 0)));

        assertEquals(new BigDecimal("150.00"), response.appliedFee());
    }

    @Test
    void rejectsMissingDeceasedWithExpectedMessage() {
        createCemetery();

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> wakeService.save(wakeRequest(999L, null, LocalTime.of(10, 0), LocalTime.of(13, 0))));

        assertEquals("Falecido não encontrado.", exception.getMessage());
    }

    @Test
    void rejectsOverlappingScheduleAndAllowsAdjacentSchedule() {
        var cemetery = createCemetery();
        var firstDeceasedId = createIdentified();
        var secondDeceasedId = createIdentified();
        var thirdDeceasedId = createIdentified();

        wakeService.save(wakeRequest(firstDeceasedId, cemetery.getId(), LocalTime.of(10, 0), LocalTime.of(13, 0)));

        var exception = assertThrows(ConflictException.class,
                () -> wakeService.save(
                        wakeRequest(secondDeceasedId, cemetery.getId(), LocalTime.of(12, 0), LocalTime.of(14, 0))));
        assertEquals("Já existe agendamento para este horário.", exception.getMessage());

        var adjacent = wakeService.save(
                wakeRequest(thirdDeceasedId, cemetery.getId(), LocalTime.of(13, 0), LocalTime.of(16, 0)));
        assertEquals(WakeStatus.SCHEDULED, adjacent.status());
    }

    @Test
    void rejectsDurationAboveCemeteryConfiguration() {
        var deceasedId = createIdentified();
        var cemetery = createCemetery();
        configureWake(cemetery, 60, false, BigDecimal.ZERO);

        assertThrows(BusinessException.class,
                () -> wakeService.save(
                        wakeRequest(deceasedId, cemetery.getId(), LocalTime.of(10, 0), LocalTime.of(12, 0))));
    }

    @Test
    void cancelsScheduledWakeAndReleasesSchedule() {
        var cemetery = createCemetery();
        var firstDeceasedId = createIdentified();
        var secondDeceasedId = createIdentified();

        var wake = wakeService.save(
                wakeRequest(firstDeceasedId, cemetery.getId(), LocalTime.of(10, 0), LocalTime.of(13, 0)));

        var cancelled = wakeService.cancel(wake.id());

        assertEquals(WakeStatus.CANCELLED, cancelled.status());
        assertThrows(BusinessException.class, () -> wakeService.cancel(wake.id()));

        var replacement = wakeService.save(
                wakeRequest(secondDeceasedId, cemetery.getId(), LocalTime.of(10, 0), LocalTime.of(13, 0)));
        assertEquals(WakeStatus.SCHEDULED, replacement.status());
    }

    private Long createIdentified() {
        var declarantId = declarantService.save(declarantRequest()).id();
        return identifiedService.save(identifiedRequest(declarantId)).id();
    }

    private CemeteryEntity createCemetery() {
        var number = nextInt();

        var cemetery = new CemeteryEntity();
        cemetery.setName("Cemiterio " + number);
        cemetery.setFoundation(LocalDate.of(1980, 1, 1));
        cemetery.setActive(true);
        return cemeteryRepository.save(cemetery);
    }

    private void configureWake(CemeteryEntity cemetery, int durationMinutes, boolean charged, BigDecimal fee) {
        var configuration = new WakeConfigurationEntity();
        configuration.setCemetery(cemetery);
        configuration.setDurationMinutes(durationMinutes);
        configuration.setCharged(charged);
        configuration.setFee(fee);
        wakeConfigurationRepository.save(configuration);
    }

    private WakeRequestDTO wakeRequest(Long deceasedId, Long cemeteryId, LocalTime startTime, LocalTime endTime) {
        return new WakeRequestDTO(
                deceasedId,
                cemeteryId,
                LocalDate.of(2026, 6, 20),
                startTime,
                endTime,
                "Velorio conforme solicitacao");
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

    private DeceasedIdentifiedRequestDTO identifiedRequest(Long declarantId) {
        var number = nextInt();
        return new DeceasedIdentifiedRequestDTO(
                "Falecido " + number,
                LocalDate.of(1970, 1, 1),
                GenderType.MALE,
                GenderIdentityType.CISGENDER,
                new DocumentInfoRequestDTO("RGF" + number, cpf(20000000000L + number)),
                "Ocupacao",
                "Pai",
                "Mae",
                "Santos",
                true,
                "Observacao",
                declarantId);
    }

    private int nextInt() {
        return (int) ++sequence;
    }

    private String cpf(long value) {
        return String.format("%011d", value % 100000000000L);
    }
}
