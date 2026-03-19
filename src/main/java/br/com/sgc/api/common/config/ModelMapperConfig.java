package br.com.sgc.api.common.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setFieldMatchingEnabled(true) // permite mapear direto pelos atributos (não só getters/setters).
                .setFieldAccessLevel(AccessLevel.PRIVATE); // permite acessar campos privados.

        return mapper;
    }
}
