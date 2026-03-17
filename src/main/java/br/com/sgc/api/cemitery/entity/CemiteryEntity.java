package br.com.sgc.api.cemitery.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "cemitery")
@Data
public class CemiteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cem_id", nullable = false)
    private Long id;

    @Column(name = "cem_name", nullable = false)
    private String name;

    @Column(name = "cem_fundation", nullable = false)
    private LocalDate fundation;

    @Column(name = "cem_active")
    private boolean active;

    @OneToMany(mappedBy = "cemitery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockEntity> blocks = new ArrayList<>();
}
