package br.com.sgc.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.json.JsonMapper;

import br.com.sgc.api.common.enums.EyeType;
import br.com.sgc.api.common.enums.GenderIdentityType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.common.enums.HairType;
import br.com.sgc.api.common.enums.SkinColor;
import br.com.sgc.api.person.dto.request.DeathRequestDTO;
import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.DeceasedUnidentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.request.support.AddressInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.ContactInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;
import br.com.sgc.api.person.service.DeathService;
import br.com.sgc.api.person.service.DeceasedIdentifiedService;
import br.com.sgc.api.person.service.DeceasedUnidentifiedService;
import br.com.sgc.api.person.service.DeclarantService;

@SpringBootTest
@AutoConfiguration
@Transactional
class LifecycleControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final JsonMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    @Autowired
    private DeclarantService declarantService;

    @Autowired
    private DeceasedIdentifiedService identifiedService;

    @Autowired
    private DeceasedUnidentifiedService unidentifiedService;

    @Autowired
    private DeathService deathService;

    private long sequence;

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deathPutAndDeleteRoutesBindPathVariable() throws Exception {
        var deceasedId = createIdentified();
        var death = deathService.save(deathRequest(deceasedId, "Casa"));

        mockMvc.perform(put("/api/death/{id}", death.id())
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deathRequest(deceasedId, "Hospital"))))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/death/{id}", death.id())
                .contextPath("/api"))
                .andExpect(status().isNoContent());
    }

    @Test
    void unidentifiedArchiveRouteBindsPathVariable() throws Exception {
        var declarantId = createDeclarant();
        var deceased = unidentifiedService.save(unidentifiedRequest(declarantId));

        mockMvc.perform(patch("/api/deceased-unidentified/{id}/archive", deceased.id())
                .contextPath("/api"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deathCreateValidationReturnsBadRequestWhenDeceasedIdIsMissing() throws Exception {
        var json = """
                {
                  "place": "Casa",
                  "date": "2025-01-01",
                  "time": "10:30:00",
                  "causeDeath": "Causa natural",
                  "doctorResponsible": "Dr. Responsavel",
                  "certificateNumber": "CERT-1",
                  "observations": "Observacao"
                }
                """;

        mockMvc.perform(post("/api/death")
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    private Long createIdentified() {
        var declarantId = createDeclarant();
        return identifiedService.save(identifiedRequest(declarantId)).id();
    }

    private Long createDeclarant() {
        return declarantService.save(declarantRequest()).id();
    }

    private DeclarantRequestDTO declarantRequest() {
        var number = nextInt();
        return new DeclarantRequestDTO(
                "Declarante " + number,
                new DocumentInfoRequestDTO("RGD" + number, cpf(10000000000L + number)),
                new ContactInfoRequestDTO("1199999" + number, "contato" + number + "@email.com"),
                new AddressInfoRequestDTO("Rua " + number, "10", "Centro", "Santos", "SP", "11000000"),
                "Analista");
    }

    private DeceasedIdentifiedRequestDTO identifiedRequest(Long declarantId) {
        var number = nextInt();
        return new DeceasedIdentifiedRequestDTO(
                "Falecido " + number,
                LocalDate.of(1970, 1, 1),
                GenderType.MALE,
                GenderIdentityType.CISGENDER,
                new DocumentInfoRequestDTO("RGF" + number, cpf(20000000000L + number)),
                "Ocupacao",
                "Pai",
                "Mae",
                "Santos",
                true,
                "Observacao",
                declarantId);
    }

    private DeceasedUnidentifiedRequestDTO unidentifiedRequest(Long declarantId) {
        return new DeceasedUnidentifiedRequestDTO(
                45,
                GenderType.MALE,
                SkinColor.BROWN,
                1.75f,
                "Preto",
                HairType.STRAIGHT,
                "Castanho",
                EyeType.ROUND,
                declarantId);
    }

    private DeathRequestDTO deathRequest(Long deceasedId, String place) {
        return new DeathRequestDTO(
                place,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 30),
                "Causa natural",
                "Dr. Responsavel",
                "CERT-" + nextInt(),
                "Observacao",
                deceasedId);
    }

    private int nextInt() {
        return (int) ++sequence;
    }

    private String cpf(long value) {
        return String.format("%011d", value % 100000000000L);
    }
}
