package br.com.sgc.api.person.entity;

import br.com.sgc.api.person.entity.embeddable.AddressInfo;
import br.com.sgc.api.person.entity.embeddable.ContatctInfo;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "declarant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeclarantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decla_id")
    private Long id;

    @Column(name = "decla_name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rg", column = @Column(name = "decla_rg", nullable = false)),
        @AttributeOverride(name = "cpf", column = @Column(name = "decla_cpf", nullable = false))
    })
    private DocumentInfo document;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "phone", column = @Column(name = "decla_phone", nullable = false)),
        @AttributeOverride(name = "email", column = @Column(name = "decla_email", nullable = false))
    })
    private ContatctInfo contatct;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "decla_street", nullable = false)),
        @AttributeOverride(name = "number", column = @Column(name = "decla_number", nullable = false)),
        @AttributeOverride(name = "district", column = @Column(name = "decla_district", nullable = false)),
        @AttributeOverride(name = "city", column = @Column(name = "decla_city", nullable = false)),
        @AttributeOverride(name = "state", column = @Column(name = "decla_state", nullable = false)),
        @AttributeOverride(name = "cep", column = @Column(name = "decla_cep", nullable = false))
    })
    private AddressInfo address;

    @Column(name = "decla_occupation", nullable = false)
    private String occupation;

}
