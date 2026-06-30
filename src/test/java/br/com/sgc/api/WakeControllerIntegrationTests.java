package br.com.sgc.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sgc.api.burial.dto.request.WakeRequestDTO;
import br.com.sgc.api.burial.service.WakeService;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.common.enums.GenderIdentityType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.request.support.AddressInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.ContactInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;
import br.com.sgc.api.person.service.DeceasedIdentifiedService;
import br.com.sgc.api.person.service.DeclarantService;

@SpringBootTest
@AutoConfiguration
@Transactional
class WakeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WakeService wakeService;

    @Autowired
    private DeclarantService declarantService;

    @Autowired
    private DeceasedIdentifiedService identifiedService;

    @Autowired
    private CemeteryRepository cemeteryRepository;

    private long sequence;

    @Test
    void wakeCreateReturnsCreatedLocationAndSuccessMessage() throws Exception {
        var deceasedId = createIdentified();
        createCemetery();

        mockMvc.perform(post("/api/wakes")
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        wakeRequest(deceasedId, null, LocalTime.of(10, 0), LocalTime.of(13, 0)))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Velório registrado com sucesso."))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void wakeCancelRouteReturnsCancelledStatus() throws Exception {
        var deceasedId = createIdentified();
        var cemetery = createCemetery();
        var wake = wakeService.save(wakeRequest(deceasedId, cemetery.getId(), LocalTime.of(10, 0), LocalTime.of(13, 0)));

        mockMvc.perform(patch("/api/wakes/{id}/cancel", wake.id())
                .contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    private Long createIdentified() {
        var declarantId = declarantService.save(declarantRequest()).id();
        return identifiedService.save(identifiedRequest(declarantId)).id();
    }

    private CemeteryEntity createCemetery() {
        var number = nextInt();

        var cemetery = new CemeteryEntity();
        cemetery.setName("Cemiterio Controller " + number);
        cemetery.setFoundation(LocalDate.of(1980, 1, 1));
        cemetery.setActive(true);
        return cemeteryRepository.save(cemetery);
    }

    private WakeRequestDTO wakeRequest(Long deceasedId, Long cemeteryId, LocalTime startTime, LocalTime endTime) {
        return new WakeRequestDTO(
                deceasedId,
                cemeteryId,
                LocalDate.of(2026, 6, 20),
                startTime,
                endTime,
                "Velorio conforme solicitacao");
    }

    private DeclarantRequestDTO declarantRequest() {
        var number = nextInt();
        return new DeclarantRequestDTO(
                "Declarante Controller " + number,
                new DocumentInfoRequestDTO("RGDC" + number, cpf(10000000000L + number)),
                new ContactInfoRequestDTO("1199999" + number, "controller" + number + "@email.com"),
                new AddressInfoRequestDTO("Rua " + number, "10", "Centro", "Santos", "SP", "11000000"),
                "Analista");
    }

    private DeceasedIdentifiedRequestDTO identifiedRequest(Long declarantId) {
        var number = nextInt();
        return new DeceasedIdentifiedRequestDTO(
                "Falecido Controller " + number,
                LocalDate.of(1970, 1, 1),
                GenderType.MALE,
                GenderIdentityType.CISGENDER,
                new DocumentInfoRequestDTO("RGFC" + number, cpf(20000000000L + number)),
                "Ocupacao",
                "Pai",
                "Mae",
                "Santos",
                true,
                "Observacao",
                declarantId);
    }

    private int nextInt() {
        return (int) ++sequence;
    }

    private String cpf(long value) {
        return String.format("%011d", value % 100000000000L);
    }
}
