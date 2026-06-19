package br.com.sgc.api.burial.entity;

import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.common.enums.WakeStatus;
import br.com.sgc.api.person.entity.DeceasedEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wake")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WakeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wak_id", nullable = false)
    private Long id;

    @Column(name = "wak_date", nullable = false)
    private LocalDate date;

    @Column(name = "wak_start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "wak_end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "wak_applied_fee", nullable = false)
    private BigDecimal appliedFee;

    @Column(name = "wak_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WakeStatus status;

    @Column(name = "wak_observations")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_wak_dec_id", nullable = false)
    private DeceasedEntity deceased;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_wak_cem_id", nullable = false)
    private CemeteryEntity cemetery;
}
