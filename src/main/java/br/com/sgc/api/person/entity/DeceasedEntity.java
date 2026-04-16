package br.com.sgc.api.person.entity;

import java.time.LocalDate;

import br.com.sgc.api.common.enums.DeceasedType;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "deceased")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeceasedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dec_id", nullable = false)
    private Long id;

    @Column(name = "dec_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "dec_type", nullable = false)
    private DeceasedType deceasedType;

    @Column(name = "dec_bith_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "dec_gender", nullable = false)
    private String gender;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rg", column = @Column(name = "dec_rg", nullable = false)),
        @AttributeOverride(name = "cpf", column = @Column(name = "dec_cpf", nullable = false))
    })
    private DocumentInfo document;

    @Column(name = "dec_ocuppation", nullable = false)
    private String occupation;

    @Column(name = "dec_fathers_name", nullable = false)
    private String fathersName;

    @Column(name = "dec_mothers_name", nullable = false)
    private String mothersName;

    @Column(name = "dec_naturalness", nullable = false)
    private String naturalness;

    @Column(name = "dec_city_resident", nullable = false)
    private boolean cityResident;

    @Column(name = "dec_observations")
    private String observations;

}
