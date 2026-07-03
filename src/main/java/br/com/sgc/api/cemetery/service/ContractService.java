package br.com.sgc.api.cemetery.service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.cemetery.dto.request.ContractGraveRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractHolderRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractUpdateRequestDTO;
import br.com.sgc.api.cemetery.dto.response.ContractResponseDTO;
import br.com.sgc.api.cemetery.entity.BlockEntity;
import br.com.sgc.api.cemetery.entity.ContractEntity;
import br.com.sgc.api.cemetery.entity.ContractHolderEntity;
import br.com.sgc.api.cemetery.entity.GraveEntity;
import br.com.sgc.api.cemetery.mapper.ContractMapper;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.ContractHolderRepository;
import br.com.sgc.api.cemetery.repositories.ContractRepository;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.ContractStatus;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.enums.GraveType;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private static final Set<ContractStatus> OPEN_STATUSES = EnumSet.of(
            ContractStatus.ACTIVE,
            ContractStatus.SUSPENDED,
            ContractStatus.OVERDUE);

    private final ContractRepository contractRepository;
    private final ContractHolderRepository holderRepository;
    private final GraveRepository graveRepository;
    private final BlockRepository blockRepository;
    private final ContractMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    @Transactional
    public ContractResponseDTO save(ContractRequestDTO request) {
        validatePeriod(request.startDate(), request.endDate());
        validateCreationStatus(request.status());
        validateGraveSelection(request.graveId(), request.newGrave());

        if (contractRepository.existsByNumber(request.number())) {
            throw new ConflictException(getMessage("contract.number.already.exists"));
        }

        var block = findBlockById(request.blockId());
        var grave = resolveGrave(request, block);
        validateGraveAvailableForContract(grave);

        var contract = mapper.toEntity(request);
        contract.setHolder(resolveHolder(request.holder()));
        contract.setGrave(grave);

        return mapper.toResponse(contractRepository.save(contract));
    }

    @Transactional(readOnly = true)
    public List<ContractResponseDTO> findAll() {
        return contractRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContractResponseDTO findById(Long id) {
        return mapper.toResponse(findContractById(id));
    }

    @Transactional
    public ContractResponseDTO update(Long id, ContractUpdateRequestDTO request) {
        var contract = findContractById(id);
        validatePeriod(request.startDate(), request.endDate());

        if (contractRepository.existsByNumberAndIdNot(request.number(), id)) {
            throw new ConflictException(getMessage("contract.number.already.exists"));
        }

        contract.setNumber(request.number());
        contract.setFee(request.fee());
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setHolder(resolveHolder(request.holder()));

        return mapper.toResponse(contractRepository.save(contract));
    }

    @Transactional
    public ContractResponseDTO suspend(Long id) {
        var contract = findContractById(id);
        requireStatus(contract, ContractStatus.ACTIVE);
        contract.setStatus(ContractStatus.SUSPENDED);
        return mapper.toResponse(contractRepository.save(contract));
    }

    @Transactional
    public ContractResponseDTO reactivate(Long id) {
        var contract = findContractById(id);

        if (contract.getStatus() == ContractStatus.EXPIRED || contract.getStatus() == ContractStatus.INACTIVE) {
            throw new BusinessException(getMessage("contract.status.transition.invalid"));
        }

        requireStatus(contract, ContractStatus.SUSPENDED);
        contract.setStatus(ContractStatus.ACTIVE);
        return mapper.toResponse(contractRepository.save(contract));
    }

    @Transactional
    public ContractResponseDTO expire(Long id) {
        var contract = findContractById(id);
        requireStatus(contract, ContractStatus.OVERDUE);
        contract.setStatus(ContractStatus.EXPIRED);
        return mapper.toResponse(contractRepository.save(contract));
    }

    @Transactional
    public ContractResponseDTO inactivate(Long id) {
        var contract = findContractById(id);
        requireStatus(contract, ContractStatus.ACTIVE);
        contract.setStatus(ContractStatus.INACTIVE);
        return mapper.toResponse(contractRepository.save(contract));
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateOverdueContracts() {
        var contracts = contractRepository.findByStatusAndEndDateBefore(ContractStatus.ACTIVE, LocalDate.now());
        contracts.forEach(contract -> contract.setStatus(ContractStatus.OVERDUE));
        contractRepository.saveAll(contracts);
    }

    // ============================================================================================
    // RESOLUTION METHODS
    // ============================================================================================

    private GraveEntity resolveGrave(ContractRequestDTO request, BlockEntity block) {
        if (request.graveId() != null) {
            var grave = graveRepository.findById(request.graveId())
                    .orElseThrow(() -> new ResourceNotFoundException(getMessage("grave.not.found")));

            if (!Objects.equals(grave.getBlock().getId(), block.getId())) {
                throw new BusinessException(getMessage("contract.grave.block.mismatch"));
            }

            return grave;
        }

        return createGrave(request.newGrave(), block);
    }

    private GraveEntity createGrave(ContractGraveRequestDTO request, BlockEntity block) {
        if (request.bodyCapacity() > 4) {
            throw new BusinessException(getMessage("grave.mausoleum.type.capacity"));
        }

        if (graveRepository.existsByNumberAndBlockId(request.number(), block.getId())) {
            throw new ConflictException(getMessage("grave.number.already.exists"));
        }

        var grave = new GraveEntity();
        grave.setNumber(request.number());
        grave.setGraveType(GraveType.MAUSOLEUM);
        grave.setBodyCapacity(request.bodyCapacity());
        grave.setAreaType(AreaType.PERPETUAL);
        grave.setActive(true);
        grave.setStatus(GraveStatus.AVAILABLE);
        grave.setBlocked(false);
        grave.setBlock(block);

        return graveRepository.save(grave);
    }

    private ContractHolderEntity resolveHolder(ContractHolderRequestDTO request) {
        var holder = holderRepository.findByCpf(request.cpf()).orElseGet(ContractHolderEntity::new);
        holder.setName(request.name());
        holder.setCpf(request.cpf());
        holder.setPhone(request.phone());
        holder.setEmail(request.email());
        holder.setActive(true);
        return holderRepository.save(holder);
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateGraveAvailableForContract(GraveEntity grave) {
        if (!grave.isActive() ||
                grave.isBlocked() ||
                grave.getGraveType() != GraveType.MAUSOLEUM ||
                grave.getAreaType() != AreaType.PERPETUAL) {
            throw new BusinessException(getMessage("contract.grave.incompatible"));
        }

        if (contractRepository.existsByGraveIdAndStatusIn(grave.getId(), OPEN_STATUSES)) {
            throw new ConflictException(getMessage("contract.grave.already.active"));
        }
    }

    private void validateCreationStatus(ContractStatus status) {
        if (status != ContractStatus.ACTIVE && status != ContractStatus.SUSPENDED) {
            throw new BusinessException(getMessage("contract.creation.status.invalid"));
        }
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (endDate == null || startDate == null || !endDate.isAfter(startDate)) {
            throw new BusinessException(getMessage("contract.invalid.period"));
        }
    }

    private void validateGraveSelection(Long graveId, ContractGraveRequestDTO newGrave) {
        if ((graveId == null && newGrave == null) || (graveId != null && newGrave != null)) {
            throw new BusinessException(getMessage("contract.grave.selection.invalid"));
        }
    }

    // ============================================================================================
    // HELPERS
    // ============================================================================================

    private void requireStatus(ContractEntity contract, ContractStatus expected) {
        if (contract.getStatus() != expected) {
            throw new BusinessException(getMessage("contract.status.transition.invalid"));
        }
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private ContractEntity findContractById(Long id) {
        if (id == null) {
            throw new BusinessException(getMessage("contract.id.required"));
        }

        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("contract.not.found")));
    }

    private BlockEntity findBlockById(Long id) {
        if (id == null) {
            throw new BusinessException(getMessage("block.id.required"));
        }

        return blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("block.not.found")));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(Objects.requireNonNull(key), null, "Messagem nao encontrada: " + key,
                LocaleContextHolder.getLocale());
    }
}
