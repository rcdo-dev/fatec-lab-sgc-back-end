package br.com.sgc.api.burial.entity;

import java.math.BigDecimal;
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

    @Column(name = "wak_start", nullable = false)
    private LocalTime startTime;

    @Column(name = "wak_end", nullable = false)
    private LocalTime endTime;

    @Column(name = "wake_fee")
    private BigDecimal fee;

    @Column(name = "wak_observations")
    private String observations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_wak_bur_id")
    private BurialEntity burial;
}
