package br.com.sgc.api.cemitery.controller;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.cemitery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemitery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemitery.entity.CemeteryEntity;
import br.com.sgc.api.cemitery.service.CemeteryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cemetery")
public class CemiteryController {
    private final CemeteryService cemeteryService;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<CemeteryResponseDTO> create(@Validated @RequestBody CemeteryRequestDTO cemeteryReq) {
        var cemetery = mapper.map(cemeteryReq, CemeteryEntity.class);
        var cemeteryCreated = mapper.map(cemeteryService.save(cemetery), CemeteryResponseDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cemeteryCreated.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cemeteryCreated);
    }

}
