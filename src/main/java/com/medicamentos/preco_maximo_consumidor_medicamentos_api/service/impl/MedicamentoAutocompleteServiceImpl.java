package com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.impl;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search.MedicamentoRepository;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.MedicamentoAutocompleteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicamentoAutocompleteServiceImpl implements MedicamentoAutocompleteService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoAutocompleteServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    public List<MedicamentoAutocompleteDTO> getAutocompleteSugestoes(String termo) {
        // 1. Busca os dados brutos do Elasticsearch
        List<Medicamento> medicamentos = medicamentoRepository.findAutocompleteSuggestions(termo);

        // 2. Mapeia a lista de Entidades (Medicamento) para uma lista de DTOs
        return medicamentos.stream()
                .map(this::convertToAutocompleteDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte uma entidade Medicamento em um MedicamentoAutocompleteDTO.
     */
    private MedicamentoAutocompleteDTO convertToAutocompleteDTO(Medicamento medicamento) {
        return new MedicamentoAutocompleteDTO(
                medicamento.getRegistroCmed(),
                medicamento.getProduto(),
                medicamento.getPrincipioAtivo(),
                medicamento.getApresentacao(),
                medicamento.getLaboratorio()
        );
    }
}
