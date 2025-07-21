package br.com.tcgpocket.cardmaker.repository;

import br.com.tcgpocket.cardmaker.model.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface CardRepository extends ReactiveMongoRepository<Card, String> {
}
