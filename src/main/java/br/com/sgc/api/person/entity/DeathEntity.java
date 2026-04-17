package br.com.sgc.api.person.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "death")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeathEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_id", nullable = false)
    private Long id;

    @Column(name = "dea_place", nullable = false)
    private String place;

    @Column(name = "dea_date", nullable = false)
    private LocalDate date;

    @Column(name = "dea_time", nullable = false)
    private LocalTime time;

    @Column(name = "dea_cause_death", nullable = false)
    private String causeDeath;

    @Column(name = "dea_doctor_responsible", nullable = false)
    private String doctorResponsible;

    @Column(name = "dea_certificate_number", nullable = false)
    private String certificateNumber;

    @Column(name = "dea_observations")
    private String observations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dea_dec_id", nullable = false, unique = true)
    private DeceasedEntity deceased;

}
