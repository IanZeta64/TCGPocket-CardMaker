package br.com.tcgpocket.cardmaker.repository.impl;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.http.PokeAPIClient;
import br.com.tcgpocket.cardmaker.model.Card;
import br.com.tcgpocket.cardmaker.repository.CardRepository;
import br.com.tcgpocket.cardmaker.vo.PokeDetailVO;
import br.com.tcgpocket.cardmaker.vo.PokeInfoVO;
import br.com.tcgpocket.cardmaker.vo.PokeSpeciesVO;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class CardDataProviderImpl implements CardDataProvider {
    private final CardRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final PokeAPIClient client;
    private final ReactiveRedisTemplate<String, PokeInfoVO> redis;
    private final Duration CACHE_TTL = Duration.ofHours(24);


    public CardDataProviderImpl(CardRepository repository, ReactiveMongoTemplate mongoTemplate, PokeAPIClient client, ReactiveRedisTemplate<String, PokeInfoVO> redis) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.client = client;
        this.redis = redis;
    }

    @Override
    public Mono<Card> save(Card card) {
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
        return (query == null) ? repository.findAll() : mongoTemplate.find(query, Card.class);
    }

    @Override
    public Mono<Card> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<PokeInfoVO> getPokeInfoFromCache(String name) {
        return redis.opsForValue().get(name);
    }
    @Override
    public Mono<PokeInfoVO> savePokeInfoInCache(String name, PokeInfoVO info) {
        return redis.opsForValue().set(name, info, CACHE_TTL)
                .flatMap(it -> Boolean.TRUE.equals(it) ? Mono.just(info) : Mono.empty()
        );
    }
}
