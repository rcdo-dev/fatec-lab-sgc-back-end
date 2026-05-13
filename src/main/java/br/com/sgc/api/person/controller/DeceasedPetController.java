package br.com.sgc.api.person.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.person.dto.request.DeceasedPetRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedPetResponseDTO;
import br.com.sgc.api.person.service.DeceasedPetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet")
@Tag(name = "Falecido Pet", description = "Operações relacionadas ao pet falecido.")
public class DeceasedPetController {
    private final DeceasedPetService deceasedPetService;

    @PostMapping
    public ResponseEntity<DeceasedPetResponseDTO> create(@Valid @RequestBody DeceasedPetRequestDTO request) {
        var response = deceasedPetService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DeceasedPetResponseDTO>> findAll() {
        return ResponseEntity.ok(deceasedPetService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeceasedPetResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deceasedPetService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeceasedPetResponseDTO> update(@PathVariable("id") Long id,
            @Valid @RequestBody DeceasedPetRequestDTO request) {
        return ResponseEntity.ok(deceasedPetService.update(id, request));
    }

    public ResponseEntity<DeceasedPetResponseDTO> archive(@PathVariable("id") Long id){
        deceasedPetService.archived(id);
        return ResponseEntity.noContent().build();
    }

}
