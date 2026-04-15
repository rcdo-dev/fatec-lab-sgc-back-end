package br.com.sgc.api.cemetery.entity;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.enums.GraveType;

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
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "grave")
@Data
public class GraveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gra_id", nullable = false)
    private Long id;

    @Column(name = "gra_number", nullable = false)
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(name = "gra_type", nullable = false)
    private GraveType graveType;

    /**
     * nullable = false → regra do banco.
     * 
     * @NotNull → regra da API -> sempre no DTO.
     */
    @NotNull
    @Column(name = "gra_body_capacity", nullable = false)
    private int bodyCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "gra_area_type", nullable = false)
    private AreaType areaType;

    @Column(name = "gra_active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "gra_status", nullable = false)
    private GraveStatus status;

    @Column(name = "gra_blocked")
    private boolean blocked;

    @Column(name = "gra_reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_gra_blo_id", nullable = false)
    private BlockEntity block;
}
