package br.com.tcgpocket.cardmaker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.tcgpocket.cardmaker")
@OpenAPIDefinition
public class CardMakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardMakerApplication.class, args);
	}

}
