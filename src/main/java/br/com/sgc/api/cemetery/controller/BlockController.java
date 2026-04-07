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

import br.com.sgc.api.cemetery.dto.request.BlockRequestDTO;
import br.com.sgc.api.cemetery.dto.request.BlockUpdateRequestDTO;
import br.com.sgc.api.cemetery.dto.response.BlockResponseDTO;

import br.com.sgc.api.cemetery.service.BlockService;

import br.com.sgc.api.common.documentation.block.CreateBlockDoc;
import br.com.sgc.api.common.documentation.block.FindAllBlockDoc;
import br.com.sgc.api.common.documentation.block.FindBlockByIdDoc;
import br.com.sgc.api.common.documentation.block.InactivateBlockDoc;
import br.com.sgc.api.common.documentation.block.UpdateBlockDoc;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

// Incluir CORS

@RestController
@RequiredArgsConstructor
@RequestMapping("/blocks")
@Tag(name = "Quadras", description = "Operações relacionadas ao cadastro de quadras.")
public class BlockController {

    private final BlockService blockService;

    @CreateBlockDoc
    @PostMapping
    public ResponseEntity<BlockResponseDTO> create(@Valid @RequestBody BlockRequestDTO request) {

        var response = blockService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @FindAllBlockDoc
    @GetMapping
    public ResponseEntity<List<BlockResponseDTO>> findAll() {
        return ResponseEntity.ok(blockService.findAll());
    }

    @FindBlockByIdDoc
    @GetMapping("/{id}")
    public ResponseEntity<BlockResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(blockService.findById(id));
    }

    @UpdateBlockDoc
    @PutMapping("/{id}")
    public ResponseEntity<BlockResponseDTO> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody BlockUpdateRequestDTO request) {
        return ResponseEntity.ok(blockService.update(id, request));
    }

    @InactivateBlockDoc
    @PatchMapping("/{id}/inactive")
    public ResponseEntity<BlockResponseDTO> inactivate(@PathVariable Long id) {
        return ResponseEntity.ok(blockService.inactivate(id));
    }

}
