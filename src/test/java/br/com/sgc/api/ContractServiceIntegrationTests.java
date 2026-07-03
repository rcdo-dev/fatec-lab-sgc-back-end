package br.com.sgc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.cemetery.dto.request.ContractGraveRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractHolderRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractRequestDTO;
import br.com.sgc.api.cemetery.entity.BlockEntity;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.entity.GraveEntity;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.cemetery.repositories.ContractHolderRepository;
import br.com.sgc.api.cemetery.repositories.ContractRepository;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.cemetery.service.ContractService;
import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.ContractStatus;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.enums.GraveType;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

@SpringBootTest
@Transactional
class ContractServiceIntegrationTests {

    @Autowired
    private ContractService contractService;

    @Autowired
    private CemeteryRepository cemeteryRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private GraveRepository graveRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractHolderRepository holderRepository;

    private long sequence;

    @Test
    void createsContractWithNewGraveUsingDomainDefaults() {
        var block = createBlock();
        var response = contractService.save(newGraveRequest("CON-001", block.getId(), 187, holder("74125896812")));

        var grave = graveRepository.findById(response.graveId()).orElseThrow();
        assertEquals(ContractStatus.ACTIVE, response.status());
        assertEquals(GraveType.MAUSOLEUM, grave.getGraveType());
        assertEquals(AreaType.PERPETUAL, grave.getAreaType());
        assertEquals(GraveStatus.AVAILABLE, grave.getStatus());
        assertTrue(grave.isActive());
        assertFalse(grave.isBlocked());
    }

    @Test
    void linksExistingCompatibleGraveAndReusesHolderByCpf() {
        var block = createBlock();
        var first = contractService.save(newGraveRequest("CON-002", block.getId(), 188, holder("11122233344")));
        var grave = createGrave(block, 189, GraveType.MAUSOLEUM, AreaType.PERPETUAL, true, false);

        var second = contractService.save(existingGraveRequest("CON-003", block.getId(), grave.getId(), holder("11122233344")));

        assertEquals(first.holder().id(), second.holder().id());
        assertEquals(1, holderRepository.count());
        assertEquals(grave.getId(), second.graveId());
    }

    @Test
    void rejectsInvalidCreationRules() {
        var block = createBlock();
        var grave = createGrave(block, 190, GraveType.EARTH, AreaType.PERPETUAL, true, false);
        contractService.save(newGraveRequest("CON-004", block.getId(), 191, holder("22233344455")));

        assertThrows(ConflictException.class,
                () -> contractService.save(newGraveRequest("CON-004", block.getId(), 192, holder("33344455566"))));
        assertThrows(BusinessException.class,
                () -> contractService.save(invalidPeriodRequest("CON-005", block.getId(), 193)));
        assertThrows(ResourceNotFoundException.class,
                () -> contractService.save(newGraveRequest("CON-006", 999999L, 194, holder("44455566677"))));
        assertThrows(BusinessException.class,
                () -> contractService.save(existingGraveRequest("CON-007", block.getId(), grave.getId(), holder("55566677788"))));
    }

    @Test
    void rejectsBlockedInactiveAndAlreadyContractedGraves() {
        var block = createBlock();
        var blocked = createGrave(block, 195, GraveType.MAUSOLEUM, AreaType.PERPETUAL, true, true);
        var inactive = createGrave(block, 196, GraveType.MAUSOLEUM, AreaType.PERPETUAL, false, false);
        var contracted = createGrave(block, 197, GraveType.MAUSOLEUM, AreaType.PERPETUAL, true, false);
        contractService.save(existingGraveRequest("CON-008", block.getId(), contracted.getId(), holder("66677788899")));

        assertThrows(BusinessException.class,
                () -> contractService.save(existingGraveRequest("CON-009", block.getId(), blocked.getId(), holder("77788899900"))));
        assertThrows(BusinessException.class,
                () -> contractService.save(existingGraveRequest("CON-010", block.getId(), inactive.getId(), holder("88899900011"))));
        assertThrows(ConflictException.class,
                () -> contractService.save(existingGraveRequest("CON-011", block.getId(), contracted.getId(), holder("99900011122"))));
    }

    @Test
    void appliesAllowedTransitionsAndBlocksReactivationOfClosedContracts() {
        var block = createBlock();
        var active = contractService.save(newGraveRequest("CON-012", block.getId(), 198, holder("10101010101")));

        var suspended = contractService.suspend(active.id());
        assertEquals(ContractStatus.SUSPENDED, suspended.status());
        assertEquals(ContractStatus.ACTIVE, contractService.reactivate(active.id()).status());

        var inactive = contractService.inactivate(active.id());
        assertEquals(ContractStatus.INACTIVE, inactive.status());
        assertThrows(BusinessException.class, () -> contractService.reactivate(active.id()));
    }

    @Test
    void marksExpiredActiveContractsAsOverdueAndExpiresOnlyOverdueContracts() {
        var block = createBlock();
        var contract = contractService.save(expiredActiveRequest("CON-013", block.getId(), 199));

        contractService.updateOverdueContracts();

        assertEquals(ContractStatus.OVERDUE, contractService.findById(contract.id()).status());
        assertEquals(ContractStatus.EXPIRED, contractService.expire(contract.id()).status());
        assertThrows(BusinessException.class, () -> contractService.reactivate(contract.id()));
    }

    @Test
    void preservesAtomicityWhenNewGraveCreationFails() {
        var block = createBlock();
        var before = graveRepository.count();

        assertThrows(BusinessException.class,
                () -> contractService.save(newGraveRequest("CON-014", block.getId(), 200, holder("12121212121"), 5)));

        assertEquals(before, graveRepository.count());
        assertFalse(contractRepository.existsByNumber("CON-014"));
    }

    private ContractRequestDTO newGraveRequest(String number, Long blockId, int graveNumber, ContractHolderRequestDTO holder) {
        return newGraveRequest(number, blockId, graveNumber, holder, 4);
    }

    private ContractRequestDTO newGraveRequest(String number, Long blockId, int graveNumber,
            ContractHolderRequestDTO holder, int bodyCapacity) {
        return new ContractRequestDTO(
                number,
                BigDecimal.valueOf(269.90),
                ContractStatus.ACTIVE,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2026, 6, 1),
                blockId,
                holder,
                null,
                new ContractGraveRequestDTO(graveNumber, bodyCapacity));
    }

    private ContractRequestDTO existingGraveRequest(String number, Long blockId, Long graveId, ContractHolderRequestDTO holder) {
        return new ContractRequestDTO(
                number,
                BigDecimal.valueOf(269.90),
                ContractStatus.ACTIVE,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2026, 6, 1),
                blockId,
                holder,
                graveId,
                null);
    }

    private ContractRequestDTO invalidPeriodRequest(String number, Long blockId, int graveNumber) {
        return new ContractRequestDTO(
                number,
                BigDecimal.valueOf(269.90),
                ContractStatus.ACTIVE,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2025, 6, 1),
                blockId,
                holder("33333333333"),
                null,
                new ContractGraveRequestDTO(graveNumber, 4));
    }

    private ContractRequestDTO expiredActiveRequest(String number, Long blockId, int graveNumber) {
        return new ContractRequestDTO(
                number,
                BigDecimal.valueOf(269.90),
                ContractStatus.ACTIVE,
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1),
                blockId,
                holder("13131313131"),
                null,
                new ContractGraveRequestDTO(graveNumber, 4));
    }

    private ContractHolderRequestDTO holder(String cpf) {
        return new ContractHolderRequestDTO("Titular " + nextInt(), cpf, "11999990000", "titular" + sequence + "@email.com");
    }

    private BlockEntity createBlock() {
        var number = nextInt();

        var cemetery = new CemeteryEntity();
        cemetery.setName("Cemiterio Contrato " + number);
        cemetery.setFoundation(LocalDate.of(1980, 1, 1));
        cemetery.setActive(true);
        cemetery = cemeteryRepository.save(cemetery);

        var block = new BlockEntity();
        block.setNumber(number);
        block.setDescription("Quadra Contrato " + number);
        block.setActive(true);
        block.setCemetery(cemetery);
        return blockRepository.save(block);
    }

    private GraveEntity createGrave(BlockEntity block, int number, GraveType graveType, AreaType areaType,
            boolean active, boolean blocked) {
        var grave = new GraveEntity();
        grave.setNumber(number);
        grave.setGraveType(graveType);
        grave.setBodyCapacity(graveType == GraveType.MAUSOLEUM ? 4 : 2);
        grave.setAreaType(areaType);
        grave.setActive(active);
        grave.setStatus(GraveStatus.AVAILABLE);
        grave.setBlocked(blocked);
        grave.setBlock(block);
        return graveRepository.save(grave);
    }

    private int nextInt() {
        return (int) ++sequence;
    }
}
