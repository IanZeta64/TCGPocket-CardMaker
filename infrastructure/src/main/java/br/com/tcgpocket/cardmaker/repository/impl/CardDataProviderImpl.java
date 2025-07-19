package br.com.tcgpocket.cardmaker.repository.impl;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.http.PokeAPIClient;
import br.com.tcgpocket.cardmaker.model.Card;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.repository.CardRepository;
import br.com.tcgpocket.cardmaker.vo.PokeDetailVO;
import br.com.tcgpocket.cardmaker.vo.PokeSpeciesVO;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CardDataProviderImpl implements CardDataProvider {
    private final CardRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final PokeAPIClient client;

    public CardDataProviderImpl(CardRepository repository, ReactiveMongoTemplate mongoTemplate, PokeAPIClient client) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.client = client;
    }

    @Override
    public Mono<PokeCard> createCard(PokeCard card) {
        return repository.save(card);
    }

    @Override
    public Mono<PokeDetailVO> getPokeDetail(String name) {
        return client.getInfoByName(name);
    }

    @Override
    public Mono<PokeSpeciesVO> getPokeSpecies(String name) {
        return client.getSpecieByName(name);
    }

    @Override
    public Flux<Card> search(Query query) {
        return mongoTemplate.find(query, Card.class);
    }
}
