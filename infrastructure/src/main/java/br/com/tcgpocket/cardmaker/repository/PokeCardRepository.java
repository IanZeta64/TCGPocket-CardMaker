package br.com.tcgpocket.cardmaker.repository;

import br.com.tcgpocket.cardmaker.repository.document.PokeCardDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface PokeCardRepository extends ReactiveMongoRepository<PokeCardDocument, String> {
}
