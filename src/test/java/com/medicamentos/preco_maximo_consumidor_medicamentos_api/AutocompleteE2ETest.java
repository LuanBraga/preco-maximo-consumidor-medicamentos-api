package com.medicamentos.preco_maximo_consumidor_medicamentos_api;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search.MedicamentoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.data.elasticsearch.repositories.index-creation-mode=none")
@DisplayName("Testes End-to-End para o fluxo de Autocomplete")
public class AutocompleteE2ETest {

    @Container
    private static final ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.14.1")
                    .withExposedPorts(9200)
                    .withEnv("xpack.security.enabled", "false");

    @TestConfiguration
    static class MockJwtDecoderConfig {
        @Bean
        public JwtDecoder jwtDecoder() {
            JwtDecoder decoder = mock(JwtDecoder.class);
            Jwt jwt = Jwt.withTokenValue("mock-token")
                    .header("alg", "none")
                    .subject("test-user")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .build();
            when(decoder.decode(anyString())).thenReturn(jwt);
            return decoder;
        }
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @BeforeAll
    static void setupIndex(@Autowired ElasticsearchOperations elasticsearchOperations) {
        var indexOps = elasticsearchOperations.indexOps(Medicamento.class);

        if (!indexOps.exists()) {
            Map<String, Object> settings = Map.of(
                    "analysis", Map.of(
                            "analyzer", Map.of(
                                    "brazilian_folding", Map.of(
                                            "tokenizer", "standard",
                                            "filter", List.of("lowercase", "asciifolding", "brazilian_stop", "brazilian_stemmer")
                                    )
                            ),
                            "filter", Map.of(
                                    "brazilian_stop", Map.of(
                                            "type", "stop",
                                            "stopwords", "_brazilian_"
                                    ),
                                    "brazilian_stemmer", Map.of(
                                            "type", "stemmer",
                                            "language", "brazilian"
                                    )
                            )
                    )
            );

            indexOps.create(settings);

            indexOps.putMapping();
        }
    }

    @BeforeEach
    void populateData() {
        medicamentoRepository.deleteAll();

        Medicamento med1 = criarMedicamento("1832602440011", "DIPIRONA", "DIPIRONA", "50 MG/ML SOL OR CT FR VD AMB X 100 ML + COP", "SANOFI MEDLEY FARMACÊUTICA LTDA");
        Medicamento med2 = criarMedicamento("1781709310011", "AAS PROTECT", "ÁCIDO ACETILSALICÍLICO", "100 MG COM REV CT BL AL AL X 30", "COSMED INDUSTRIA DE COSMETICOS E MEDICAMENTOS S.A..");
        medicamentoRepository.saveAll(List.of(med1, med2));
    }

    @Test
    @DisplayName("Deve buscar por 'dipi' e retornar o resultado correto via API")
    void deveBuscarSugestoesERetornarResultadoCorreto() {
        String termo = "dipirona";
        String url = String.format("/api/autocomplete-medicamento?termo=%s&page=0&size=10", termo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer mock-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody()).contains("\"totalElements\":1");
        assertThat(response.getBody()).contains("\"produto\":\"DIPIRONA\"");
        assertThat(response.getBody()).contains("\"principioAtivo\":\"DIPIRONA\"");
        assertThat(response.getBody()).contains("\"apresentacao\":\"50 MG/ML SOL OR CT FR VD AMB X 100 ML + COP\"");
        assertThat(response.getBody()).contains("\"laboratorio\":\"SANOFI MEDLEY FARMACÊUTICA LTDA\"");
        assertThat(response.getBody()).doesNotContain("AAS PROTECT");
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