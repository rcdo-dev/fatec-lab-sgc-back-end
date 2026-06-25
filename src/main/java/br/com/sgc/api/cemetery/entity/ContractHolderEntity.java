package br.com.sgc.api.cemetery.entity;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contract_holder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractHolderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conh_id", nullable = false)
    private Long id;

    @Column(name = "conh_name", nullable = false)
    private String name;

    @Column(name = "conh_cpf", nullable = false)
    private String cpf;

    @Column(name = "conh_phone", nullable = false)
    private String phone;

    @Column(name = "conh_email", nullable = false)
    private String email;

    @Column(name = "conh_active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractEntity> contratcts = new ArrayList<>();
}
