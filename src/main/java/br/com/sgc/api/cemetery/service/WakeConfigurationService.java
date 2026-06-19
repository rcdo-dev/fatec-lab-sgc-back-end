package br.com.sgc.api.cemetery.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.entity.WakeConfigurationEntity;
import br.com.sgc.api.cemetery.repositories.WakeConfigurationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WakeConfigurationService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final WakeConfigurationRepository wakeConfigurationRepository;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public WakeConfigurationEntity createForCemetery(
            CemeteryEntity cemetery,
            Integer durationMinutes,
            Boolean charged,
            BigDecimal fee) {
        var configuration = new WakeConfigurationEntity();
        configuration.setCemetery(cemetery);
        applyConfiguration(configuration, durationMinutes, charged, fee);
        return configuration;
    }

    public void updateForCemetery(
            CemeteryEntity cemetery,
            Integer durationMinutes,
            Boolean charged,
            BigDecimal fee) {
        var configuration = cemetery.getWakeConfiguration();

        if (configuration == null) {
            configuration = createForCemetery(cemetery, durationMinutes, charged, fee);
            cemetery.setWakeConfiguration(configuration);
            return;
        }

        applyConfiguration(configuration, durationMinutes, charged, fee);
    }

    public WakeConfigurationEntity findOrCreateForCemetery(CemeteryEntity cemetery) {
        return wakeConfigurationRepository.findByCemeteryId(cemetery.getId()).orElseGet(() -> {
            var configuration = createForCemetery(cemetery, null, null, null);
            return wakeConfigurationRepository.save(configuration);
        });
    }

    public BigDecimal resolveAppliedFee(WakeConfigurationEntity configuration) {
        return configuration.isCharged() ? configuration.getFee() : BigDecimal.ZERO;
    }

    // ============================================================================================
    // PRIVATE METHODS
    // ============================================================================================

    private void applyConfiguration(
            WakeConfigurationEntity configuration,
            Integer durationMinutes,
            Boolean charged,
            BigDecimal fee) {
        configuration.setDurationMinutes(durationMinutes == null
                ? WakeConfigurationEntity.DEFAULT_DURATION_MINUTES
                : durationMinutes);
        configuration.setCharged(Boolean.TRUE.equals(charged));
        configuration.setFee(fee == null ? WakeConfigurationEntity.DEFAULT_FEE : fee);
    }
}
