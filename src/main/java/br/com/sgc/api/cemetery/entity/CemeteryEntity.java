package br.com.sgc.api.cemetery.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cemetery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cem_id", nullable = false)
    private Long id;

    @Column(name = "cem_name", nullable = false)
    private String name;

    @Column(name = "cem_foundation_date", nullable = false)
    private LocalDate foundation;

    @Column(name = "cem_active")
    private boolean active;

    /**
     * mappedBy = "cemitery" -> "quem manda na relação é o atributo 'cemitery' dentro de BlockEntity".
     * fetch = FetchType.LAZY -> não carregar automaticamente a lista de blocks.
     * cascade = CascadeType.ALL -> operações no cemitério afetam as quadras.
     * orphanRemoval = true -> se remover da lista → apaga do banco.
     */

    @OneToMany(mappedBy = "cemetery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockEntity> blocks = new ArrayList<>();

    @OneToOne(mappedBy = "cemetery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private WakeConfigurationEntity wakeConfiguration;
}
