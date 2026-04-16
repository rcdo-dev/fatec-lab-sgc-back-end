package br.com.sgc.api.person.entity.embeddable;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AddressInfo {
    private String street;
    private String number;
    private String district;
    private String city;
    private String state;
    private String cep;
}
