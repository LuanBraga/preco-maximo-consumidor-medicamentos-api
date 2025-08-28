package com.medicamentos.preco_maximo_consumidor_medicamentos_api.model.search;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.InnerField;

@Getter
@Setter
@Document(indexName = "medicamentos")
public class Medicamento {

    @Id
    @Field(name = "REGISTRO_CMED", type = FieldType.Keyword)
    private String registroCmed;

    @MultiField(
            mainField = @Field(name = "PRODUTO", type = FieldType.Text, analyzer = "brazilian"), // <-- CORREÇÃO APLICADA
            otherFields = {
                    @InnerField(suffix = "suggest", type = FieldType.Search_As_You_Type)
            }
    )
    private String produto;

    @MultiField(
            mainField = @Field(name = "PRINCIPIO_ATIVO", type = FieldType.Text, analyzer = "brazilian"), // <-- CORREÇÃO APLICADA
            otherFields = {
                    @InnerField(suffix = "suggest", type = FieldType.Search_As_You_Type)
            }
    )
    private String principioAtivo;

    @MultiField(
            mainField = @Field(name = "APRESENTACAO", type = FieldType.Text, analyzer = "brazilian"), // <-- CORREÇÃO APLICADA
            otherFields = {
                    @InnerField(suffix = "suggest", type = FieldType.Search_As_You_Type)
            }
    )
    private String apresentacao;

    @MultiField(
            mainField = @Field(name = "LABORATORIO", type = FieldType.Text, analyzer = "brazilian"), // <-- CORREÇÃO APLICADA
            otherFields = {
                    @InnerField(suffix = "suggest", type = FieldType.Search_As_You_Type)
            }
    )
    private String laboratorio;

    @Field(name = "PRINCIPIO_ATIVO_UNICO", type = FieldType.Boolean)
    private Boolean principioAtivoUnico;
}