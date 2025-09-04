package com.medicamentos.preco_maximo_consumidor_medicamentos_api.service.impl;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.dto.MedicamentoAutocompleteDTO;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MedicamentoAutocompleteServiceImplTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoAutocompleteServiceImpl medicamentoAutocompleteService;

    private Medicamento medicamento;
    private Page<Medicamento> paginaDeMedicamentos;

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
        medicamento.setId("1");
        medicamento.setProduto("Produto Teste");
        medicamento.setPrincipioAtivo("Principio Ativo Teste");
        medicamento.setApresentacao("Apresentacao Teste");
        medicamento.setLaboratorio("Laboratorio Teste");

        List<Medicamento> listaMedicamentos = Collections.singletonList(medicamento);
        paginaDeMedicamentos = new PageImpl<>(listaMedicamentos, PageRequest.of(0, 10), 1);
    }

    @Test
    void aoBuscarSugestoes_deveChamarRepositorioEMapearParaDTO() {
        // Dado (Given)
        String termo = "paracetamol";
        Pageable pageable = PageRequest.of(0, 10);
        given(medicamentoRepository.findAutocompleteSuggestions(termo, pageable)).willReturn(paginaDeMedicamentos);

        // Quando (When)
        Page<MedicamentoAutocompleteDTO> resultado = medicamentoAutocompleteService.getAutocompleteSugestoes(termo, pageable);

        // Então (Then)
        // Confirma que o método do repositório foi chamado
        verify(medicamentoRepository, times(1)).findAutocompleteSuggestions(termo, pageable);

        // Valida o mapeamento
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent()).hasSize(1);

        MedicamentoAutocompleteDTO dto = resultado.getContent().get(0);
        assertThat(dto.getId()).isEqualTo(medicamento.getId());
        assertThat(dto.getProduto()).isEqualTo(medicamento.getProduto());
        assertThat(dto.getPrincipioAtivo()).isEqualTo(medicamento.getPrincipioAtivo());
        assertThat(dto.getApresentacao()).isEqualTo(medicamento.getApresentacao());
        assertThat(dto.getLaboratorio()).isEqualTo(medicamento.getLaboratorio());
    }
}
