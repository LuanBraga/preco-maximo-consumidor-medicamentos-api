package com.medicamentos.preco_maximo_consumidor_medicamentos_api.controller;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.MedicamentoAutocompleteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autocomplete-medicamento")
public class MedicamentoAutocompleteController {

    private final MedicamentoAutocompleteService medicamentoAutocompleteService;

    public MedicamentoAutocompleteController(MedicamentoAutocompleteService medicamentoAutocompleteService) {
        this.medicamentoAutocompleteService = medicamentoAutocompleteService;
    }

    @GetMapping
    public ResponseEntity<Page<MedicamentoAutocompleteDTO>> getSuggestions(
            @RequestParam String termo,
            Pageable pageable) {

        Page<MedicamentoAutocompleteDTO> sugestoesPaginadas =
                medicamentoAutocompleteService.getAutocompleteSugestoes(termo, pageable);

        return ResponseEntity.ok(sugestoesPaginadas);
    }
}