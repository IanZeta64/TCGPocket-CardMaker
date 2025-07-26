package br.com.tcgpocket.cardmaker.dataprovider;

import br.com.tcgpocket.cardmaker.model.Card;
import br.com.tcgpocket.cardmaker.vo.PokeDetailVO;
import br.com.tcgpocket.cardmaker.vo.PokeSpeciesVO;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardDataProvider {

    Mono<Card> save(Card card);

    Mono<PokeDetailVO> getPokeDetail(String name);

    Mono<PokeSpeciesVO> getPokeSpecies(String name);

    Flux<Card> search(Query query);

    Mono<Card> getById(String id);

    Mono<Boolean> existsById(String id);

    Mono<Void> delete(String id);
}
