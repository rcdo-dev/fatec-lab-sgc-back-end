package br.com.sgc.api.burial.entity;

import java.time.LocalDate;
import br.com.sgc.api.common.enums.ExhumationDispositionType;
import br.com.sgc.api.common.enums.ExhumationPurpose;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "exhumation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhumationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exh_id", nullable = false)
    private Long id;

    @Column(name = "exh_purpose", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExhumationPurpose purpose;

    @Column(name = "exh_date_time", nullable = false)
    private LocalDate exhumedAt;

    // Incluir e relacionar possíveis locais de destino.
    @Column(name = "exh_disposition_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExhumationDispositionType dispositionType;

    @Column(name = "exh_observations")
    private String observations;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_exh_exhi_id", nullable = false, unique = true)
    private ExhumationInspectionEntity inspection;
}
