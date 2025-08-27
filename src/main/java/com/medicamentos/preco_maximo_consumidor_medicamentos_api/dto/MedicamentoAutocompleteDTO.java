package com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoAutocompleteDTO {

    private String registroCmed;
    private String produto;
    private String principioAtivo;
    private String apresentacao;
    private String laboratorio;

}
