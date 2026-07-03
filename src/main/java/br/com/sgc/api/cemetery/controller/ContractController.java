package br.com.sgc.api.cemetery.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.cemetery.dto.request.ContractRequestDTO;
import br.com.sgc.api.cemetery.dto.request.ContractUpdateRequestDTO;
import br.com.sgc.api.cemetery.dto.response.ContractResponseDTO;
import br.com.sgc.api.cemetery.service.ContractService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contracts")
@Tag(name = "Contratos", description = "Operacoes relacionadas ao gerenciamento de contratos.")
public class ContractController {
    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponseDTO> create(@Valid @RequestBody ContractRequestDTO request) {
        var response = contractService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> findAll() {
        return ResponseEntity.ok(contractService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contractService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ContractUpdateRequestDTO request) {
        return ResponseEntity.ok(contractService.update(id, request));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ContractResponseDTO> suspend(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contractService.suspend(id));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<ContractResponseDTO> reactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contractService.reactivate(id));
    }

    @PatchMapping("/{id}/expire")
    public ResponseEntity<ContractResponseDTO> expire(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contractService.expire(id));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<ContractResponseDTO> inactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contractService.inactivate(id));
    }
}
