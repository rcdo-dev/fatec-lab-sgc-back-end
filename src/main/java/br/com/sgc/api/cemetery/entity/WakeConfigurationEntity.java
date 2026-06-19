package br.com.sgc.api.cemetery.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wake_configuration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WakeConfigurationEntity {

    public static final int DEFAULT_DURATION_MINUTES = 180;
    public static final BigDecimal DEFAULT_FEE = BigDecimal.ZERO;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wakc_id", nullable = false)
    private Long id;

    @Column(name = "wakc_duration_minutes", nullable = false)
    private int durationMinutes = DEFAULT_DURATION_MINUTES;

    @Column(name = "wakc_charged", nullable = false)
    private boolean charged;

    @Column(name = "wakc_fee", nullable = false)
    private BigDecimal fee = DEFAULT_FEE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_wakc_cem_id", nullable = false, unique = true)
    private CemeteryEntity cemetery;
}
