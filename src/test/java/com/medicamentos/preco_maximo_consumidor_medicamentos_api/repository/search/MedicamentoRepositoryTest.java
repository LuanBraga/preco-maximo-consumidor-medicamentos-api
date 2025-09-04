package com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataElasticsearchTest
@EnableElasticsearchRepositories(basePackageClasses = MedicamentoRepository.class)
@DisplayName("Testes de Integração para MedicamentoRepository")
class MedicamentoRepositoryTest {

    @Container
    private static final ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.14.1")
                    .withExposedPorts(9200)
                    .withEnv("xpack.security.enabled", "false");

    @TestConfiguration
    static class TestConfig extends ElasticsearchConfiguration {
        @Override
        public ClientConfiguration clientConfiguration() {
            return ClientConfiguration.builder()
                    .connectedTo(elasticsearchContainer.getHttpHostAddress())
                    .build();
        }
    }

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeEach
    void setUp() {
        // 1. Deleta o índice se ele existir
        if (elasticsearchOperations.indexOps(Medicamento.class).exists()) {
            elasticsearchOperations.indexOps(Medicamento.class).delete();
        }

        // 2. Define as configurações do analisador usando um Map
        Map<String, Object> settings = Map.of(
                "analysis", Map.of(
                        "analyzer", Map.of(
                                "brazilian_folding", Map.of(
                                        "tokenizer", "standard",
                                        "filter", List.of("lowercase", "asciifolding")
                                )
                        )
                )
        );

        // 3. Cria o índice com o mapeamento da entidade E as configurações do analisador
        elasticsearchOperations.indexOps(Medicamento.class).create(settings);
        elasticsearchOperations.indexOps(Medicamento.class).putMapping();


        // 4. Salva os dados de teste
        Medicamento med1 = criarMedicamento("1", "DIPIRONA SODICA 500MG", "DIPIRONA SODICA", "COMPRIMIDO", "PRATI-DONADUZZI");
        Medicamento med2 = criarMedicamento("2", "PARACETAMOL 750MG", "PARACETAMOL", "COMPRIMIDO", "EMS");
        Medicamento med3 = criarMedicamento("3", "DIPIRONA MONOIDRATADA GOTAS", "DIPIRONA MONOIDRATADA", "SOLUCAO ORAL", "NEO QUIMICA");

        medicamentoRepository.saveAll(List.of(med1, med2, med3));
    }

    @Test
    @DisplayName("Deve encontrar medicamentos pelo início do nome do produto")
    void findAutocompleteSuggestions_deveRetornarMedicamentosPorTermo() {
        // Act
        Page<Medicamento> resultado = medicamentoRepository.findAutocompleteSuggestions("dipi", PageRequest.of(0, 5));

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(2);
        assertThat(resultado.getContent())
                .extracting(Medicamento::getProduto)
                .containsExactlyInAnyOrder("DIPIRONA SODICA 500MG", "DIPIRONA MONOIDRATADA GOTAS");
    }

    private Medicamento criarMedicamento(String id, String produto, String principioAtivo, String apresentacao, String laboratorio) {
        Medicamento med = new Medicamento();
        med.setId(id);
        med.setProduto(produto);
        med.setPrincipioAtivo(principioAtivo);
        med.setApresentacao(apresentacao);
        med.setLaboratorio(laboratorio);
        return med;
    }
}