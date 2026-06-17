package com.unlar.guarderia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class GuarderiaApplication {

	public static void main(String[] args) {
		// 1. Cargamos el archivo .env manualmente
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        
        // 2. Inyectamos las variables de entorno directamente en el sistema de Java
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
		SpringApplication.run(GuarderiaApplication.class, args);
	}

}
