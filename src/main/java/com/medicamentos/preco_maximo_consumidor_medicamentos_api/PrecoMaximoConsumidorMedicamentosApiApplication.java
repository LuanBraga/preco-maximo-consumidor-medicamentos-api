package com.medicamentos.preco_maximo_consumidor_medicamentos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class PrecoMaximoConsumidorMedicamentosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrecoMaximoConsumidorMedicamentosApiApplication.class, args);
	}

}
