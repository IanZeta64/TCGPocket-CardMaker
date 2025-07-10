package br.com.tcgpocket.cardmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.tcgpocket.cardmaker")
public class CardMakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardMakerApplication.class, args);
	}

}
