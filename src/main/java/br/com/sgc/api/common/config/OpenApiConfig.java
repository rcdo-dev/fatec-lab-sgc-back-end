package br.com.sgc.api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SGC API")
                        .description("API Back-end do Sistema de Gerenciamento de Cemitérios")
                        .version("v1")
                        .contact(new Contact()
                                .name("Equipe SGC")
                                .email("contato@email.com"))
                        .license(new License()
                                .name("Uso acadêmico e institucional")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação do projeto SGC"));
    }
}
