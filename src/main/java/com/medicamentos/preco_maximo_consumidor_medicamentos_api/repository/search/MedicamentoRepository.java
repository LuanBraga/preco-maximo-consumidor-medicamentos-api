package com.medicamentos.preco_maximo_consumidor_medicamentos_api.repository.search;

import com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search.Medicamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends ElasticsearchRepository<Medicamento, String> {

    @Query("{" +
            "  \"bool\": {" +
            "    \"must\": [" +
            "      {" +
            "        \"multi_match\": {" +
            "          \"query\": \"?0\"," +
            "          \"type\": \"bool_prefix\"," +
            "          \"analyzer\": \"brazilian_folding\"," +
            "          \"fields\": [" +
            "            \"PRODUTO.suggest^3\"," +
            "            \"PRINCIPIO_ATIVO.suggest^2\"," +
            "            \"APRESENTACAO.suggest^5\"," +
            "            \"LABORATORIO.suggest^0.5\"" +
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
            "      }," +
            "      {" +
            "        \"multi_match\": {" +
            "          \"query\": \"?0\"," +
            "          \"type\": \"phrase\"," +
            "          \"fields\": [" +
            "            \"PRODUTO\"," +
            "            \"PRINCIPIO_ATIVO\"" +
            "          ]," +
            "          \"boost\": 50" +
            "        }" +
            "      }" +
            "    ]" +
            "  }" +
            "}")
    Page<Medicamento> findAutocompleteSuggestions(String termo, Pageable pageable);
}