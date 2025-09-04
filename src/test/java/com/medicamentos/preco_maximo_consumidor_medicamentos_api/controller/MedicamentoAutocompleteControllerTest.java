package com.medicamentos.preco_maximo_consumidor_medicamentos_api.controller;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.MedicamentoAutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicamentoAutocompleteController.class)
@WithMockUser
class MedicamentoAutocompleteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoAutocompleteService medicamentoAutocompleteService;

    private Page<MedicamentoAutocompleteDTO> sugestoesPaginadas;

    @BeforeEach
    void setUp() {
        MedicamentoAutocompleteDTO dto = new MedicamentoAutocompleteDTO("1", "Produto Teste", "Principio Ativo Teste", "Apresentacao Teste", "Laboratorio Teste");
        List<MedicamentoAutocompleteDTO> listaSugestoes = Collections.singletonList(dto);
        sugestoesPaginadas = new PageImpl<>(listaSugestoes, PageRequest.of(0, 10), 1);
    }

    @Test
    void aoBuscarSugestoes_deveChamarServicoERetornarStatusOK() throws Exception {
        // Dado (Given)
        String termo = "dipirona";
        Pageable pageable = PageRequest.of(0, 10);
        given(medicamentoAutocompleteService.getAutocompleteSugestoes(termo, pageable)).willReturn(sugestoesPaginadas);

        // Quando (When) & Então (Then)
        mockMvc.perform(get("/api/autocomplete-medicamento")
                        .param("termo", termo)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].produto", is("Produto Teste")))
                .andExpect(jsonPath("$.page.totalElements", is(1)));

        // Verifica se o método do serviço foi chamado exatamente uma vez com os parâmetros corretos
        verify(medicamentoAutocompleteService, times(1)).getAutocompleteSugestoes(termo, pageable);
    }

    @Test
    void aoBuscarSugestoesSemTermo_deveRetornarBadRequest() throws Exception {
        // Cenário onde o parâmetro 'termo' é obrigatório e não é fornecido
        mockMvc.perform(get("/api/autocomplete-medicamento")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Garante que o serviço não foi chamado
        verify(medicamentoAutocompleteService, times(0)).getAutocompleteSugestoes(anyString(), any(Pageable.class));
    }
}