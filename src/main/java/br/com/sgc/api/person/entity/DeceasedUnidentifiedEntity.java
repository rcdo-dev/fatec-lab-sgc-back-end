package br.com.sgc.api.person.entity;

import br.com.sgc.api.common.enums.EyeType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.common.enums.HairType;
import br.com.sgc.api.common.enums.SkinColor;

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
@Table(name = "deceased_unidentified")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeceasedUnidentifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decu_id", nullable = false)
    private Long id;

    @Column(name = "decu_estimated_age", nullable = false)
    private String estimatedAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "decu_gender", nullable = false)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "skin_color", nullable = false)
    private SkinColor skinColor;

    @Column(name = "stature", nullable = false)
    private float stature;

    @Column(name = "hair_color", nullable = false)
    private String hairColor;

    @Column(name = "hair_type", nullable = false)
    private HairType hairType;

    @Column(name = "eye_color", nullable = false)
    private String eyeColor;

    @Column(name = "eye_type", nullable = false)
    private EyeType eyeType;

}
