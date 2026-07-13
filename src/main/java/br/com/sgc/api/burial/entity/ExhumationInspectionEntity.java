package br.com.sgc.api.burial.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.sgc.api.common.enums.DecompositionStatus;

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
@Table(name = "exhumation_inspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhumationInspectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhi_id", nullable = false)
    private Long id;

    @Column(name = "exhi_inspected_at", nullable = false)
    private LocalDateTime inspectedAt;

    @Column(name = "exhi_decomposition_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DecompositionStatus decompositionStatus;

    @Column(name = "exhi_next_eligible_date")
    private LocalDate nextEligibleDate;

    @Column(name = "exhi_observations")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_exhi_bur_id", nullable = false)
    private BurialEntity burial;

}
