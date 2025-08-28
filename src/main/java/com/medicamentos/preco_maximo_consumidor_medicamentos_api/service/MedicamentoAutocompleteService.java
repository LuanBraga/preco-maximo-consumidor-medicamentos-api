package com.medicamentos.preco_maximo_consumidor_medicamentos_api.service;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MedicamentoAutocompleteService {

    /**
     * Busca sugestões de autocomplete com base em um termo, de forma paginada.
     *
     * @param termo O texto parcial para a busca.
     * @param pageable Objeto contendo informações de paginação.
     * @return Uma página de DTOs com as sugestões encontradas.
     */
    Page<MedicamentoAutocompleteDTO> getAutocompleteSugestoes(String termo, Pageable pageable);

}