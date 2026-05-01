package br.com.sgc.api.person.entity;

import java.time.LocalDate;

import br.com.sgc.api.common.enums.GenderIdentityType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "deceased_identified")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeceasedIdentifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deci_id", nullable = false)
    private Long id;

    @Column(name = "deci_name", nullable = false)
    private String name;

    @Column(name = "deci_bith_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "deci_gender", nullable = false)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "deci_gender_identity", nullable = false)
    private GenderIdentityType genderIdentity;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rg", column = @Column(name = "deci_rg", nullable = false)),
        @AttributeOverride(name = "cpf", column = @Column(name = "deci_cpf", nullable = false))
    })
    private DocumentInfo document;

    @Column(name = "deci_ocuppation", nullable = false)
    private String occupation;

    @Column(name = "deci_fathers_name", nullable = false)
    private String fathersName;

    @Column(name = "deci_mothers_name", nullable = false)
    private String mothersName;

    @Column(name = "deci_naturalness", nullable = false)
    private String naturalness;

    @Column(name = "deci_city_resident", nullable = false)
    private boolean cityResident;

    @Column(name = "deci_observations")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_deci_decla_id", nullable = false)
    private DeclarantEntity declarant;
}
