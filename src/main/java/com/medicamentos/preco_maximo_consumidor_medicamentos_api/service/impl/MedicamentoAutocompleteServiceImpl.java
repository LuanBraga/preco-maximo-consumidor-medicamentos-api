package com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.impl;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search.MedicamentoRepository;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.MedicamentoAutocompleteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicamentoAutocompleteServiceImpl implements MedicamentoAutocompleteService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoAutocompleteServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    public Page<MedicamentoAutocompleteDTO> getAutocompleteSugestoes(String termo, Pageable pageable) {
        // 1. Busca os dados paginados do Elasticsearch
        Page<Medicamento> medicamentosPage = medicamentoRepository.findAutocompleteSuggestions(termo, pageable);

        // 2. Mapeia a página de Entidades (Medicamento) para uma página de DTOs
        return medicamentosPage.map(this::convertToAutocompleteDTO);
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