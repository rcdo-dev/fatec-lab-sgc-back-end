package br.com.sgc.api.person.entity;

import br.com.sgc.api.common.enums.GenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "deceased_pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeceasedPetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decp_id", nullable = false)
    private Long id;

    @Column(name = "decp_name", nullable = false)
    private String name;

    @Column(name = "decp_species", nullable = false)
    private String species;

    @Column(name = "decp_breed", nullable = false)
    private String Breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "decp_sex", nullable = false)
    private GenderType sex;

    @Column(name = "decp_color", nullable = false)
    private String color;

    @Column(name = "decp_estimated_age", nullable = false)
    private int estimatedAge;
}
