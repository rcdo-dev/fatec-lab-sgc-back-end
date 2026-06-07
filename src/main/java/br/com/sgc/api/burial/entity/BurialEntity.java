package br.com.sgc.api.burial.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.sgc.api.cemetery.entity.GraveEntity;
import br.com.sgc.api.common.enums.BurialStatus;
import br.com.sgc.api.person.entity.DeceasedEntity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "burial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BurialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bur_id", nullable = false)
    private Long id;

    @Column(name = "bur_date", nullable = false)
    private LocalDate date;

    @Column(name = "bur_time", nullable = false)
    private LocalTime time;

    @Column(name = "bur_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BurialStatus status;

    @Column(name = "bur_observations", nullable = false)
    private String observations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bur_dec_id", nullable = false)
    private DeceasedEntity deceased;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bur_gra_id", nullable = false)
    private GraveEntity grave;

}
