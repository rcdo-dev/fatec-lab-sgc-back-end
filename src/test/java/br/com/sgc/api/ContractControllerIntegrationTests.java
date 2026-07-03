package br.com.sgc.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

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

import br.com.sgc.api.cemetery.dto.request.ContractGraveRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractHolderRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractUpdateRequestDTO;
import br.com.sgc.api.cemetery.entity.BlockEntity;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.cemetery.service.ContractService;
import br.com.sgc.api.common.enums.ContractStatus;

@SpringBootTest
@AutoConfiguration
@Transactional
class ContractControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final JsonMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    @Autowired
    private ContractService contractService;

    @Autowired
    private CemeteryRepository cemeteryRepository;

    @Autowired
    private BlockRepository blockRepository;

    private long sequence;

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void contractCreateReturnsCreatedContract() throws Exception {
        var block = createBlock();

        mockMvc.perform(post("/api/contracts")
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractRequest("HTTP-001", block.getId(), 301))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("HTTP-001"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.graveId").exists())
                .andExpect(jsonPath("$.blockId").value(block.getId()));
    }

    @Test
    void contractGetUpdateAndSuspendRoutesWork() throws Exception {
        var block = createBlock();
        var contract = contractService.save(contractRequest("HTTP-002", block.getId(), 302));

        mockMvc.perform(get("/api/contracts/{id}", contract.id()).contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("HTTP-002"));

        mockMvc.perform(put("/api/contracts/{id}", contract.id())
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest("HTTP-002-A"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("HTTP-002-A"));

        mockMvc.perform(patch("/api/contracts/{id}/suspend", contract.id()).contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUSPENDED"));
    }

    @Test
    void contractCreateValidationAndConflictReturnExpectedStatuses() throws Exception {
        var block = createBlock();
        contractService.save(contractRequest("HTTP-003", block.getId(), 303));

        mockMvc.perform(post("/api/contracts")
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractRequest("HTTP-003", block.getId(), 304))))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/contracts")
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/contracts")
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractRequest("HTTP-004", 999999L, 305))))
                .andExpect(status().isNotFound());
    }

    private ContractRequestDTO contractRequest(String number, Long blockId, int graveNumber) {
        return new ContractRequestDTO(
                number,
                BigDecimal.valueOf(269.90),
                ContractStatus.ACTIVE,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2026, 6, 1),
                blockId,
                holder(),
                null,
                new ContractGraveRequestDTO(graveNumber, 4));
    }

    private ContractUpdateRequestDTO updateRequest(String number) {
        return new ContractUpdateRequestDTO(
                number,
                BigDecimal.valueOf(399.90),
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2026, 6, 1),
                holder());
    }

    private ContractHolderRequestDTO holder() {
        var number = nextInt();
        return new ContractHolderRequestDTO(
                "Titular Controller " + number,
                cpf(30000000000L + number),
                "11999990000",
                "contrato" + number + "@email.com");
    }

    private BlockEntity createBlock() {
        var number = nextInt();

        var cemetery = new CemeteryEntity();
        cemetery.setName("Cemiterio Contrato Controller " + number);
        cemetery.setFoundation(LocalDate.of(1980, 1, 1));
        cemetery.setActive(true);
        cemetery = cemeteryRepository.save(cemetery);

        var block = new BlockEntity();
        block.setNumber(number);
        block.setDescription("Quadra Contrato Controller " + number);
        block.setActive(true);
        block.setCemetery(cemetery);
        return blockRepository.save(block);
    }

    private int nextInt() {
        return (int) ++sequence;
    }

    private String cpf(long value) {
        return String.format("%011d", value % 100000000000L);
    }
}
