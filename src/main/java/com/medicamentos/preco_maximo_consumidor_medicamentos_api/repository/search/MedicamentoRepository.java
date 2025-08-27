package com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentoRepository extends ElasticsearchRepository<Medicamento, String> {

    @Query("{" +
            "  \"bool\": {" +
            "    \"must\": [" +
            "      {" +
            "        \"multi_match\": {" +
            "          \"query\": \"?0\"," +
            "          \"type\": \"cross_fields\"," +
            "          \"operator\": \"AND\"," +
            "          \"fields\": [" +
            "            \"PRODUTO.suggest^3\"," +
            "            \"PRINCIPIO_ATIVO.suggest^2\"," +
            "            \"APRESENTACAO.suggest^1\"," +
            "            \"LABORATORIO.suggest^0.5\"," +
            "            \"PRODUTO^3\"," +
            "            \"PRINCIPIO_ATIVO^2\"," +
            "            \"APRESENTACAO^1\"," +
            "            \"LABORATORIO^0.5\"" +
            "          ]" +
            "        }" +
            "      }" +
            "    ]," +
            "    \"should\": [" +
            "      {" +
            "        \"bool\": {" +
            "          \"must\": [" +
            "            { \"match\": { \"PRINCIPIO_ATIVO\": \"?0\" } }" +
            "          ]," +
            "          \"filter\": [" +
            "            { \"term\": { \"PRINCIPIO_ATIVO_UNICO\": true } }" +
            "          ]," +
            "          \"boost\": 10" +
            "        }" +
            "      }" +
            "    ]" +
            "  }" +
            "}")
    List<Medicamento> findAutocompleteSuggestions(String termo);
}