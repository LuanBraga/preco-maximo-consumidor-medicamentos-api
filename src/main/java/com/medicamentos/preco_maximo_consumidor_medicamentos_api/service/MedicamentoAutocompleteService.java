package com.medicamentos.preco_maximo_consumidor_medicamentos_api.service;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;

import java.util.List;

public interface MedicamentoAutocompleteService {

    /**
     * Busca sugestões de autocomplete com base em um termo.
     *
     * @param termo O texto parcial para a busca.
     * @return Uma lista de DTOs com as sugestões encontradas.
     */
    List<MedicamentoAutocompleteDTO> getAutocompleteSugestoes(String termo);

}
