package br.com.tcgpocket.cardmaker.dataprovider;

import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.vo.PokeDetailVO;
import br.com.tcgpocket.cardmaker.vo.PokeSpeciesVO;
import reactor.core.publisher.Mono;

public interface PokeCardDataProvider {
    Mono<PokeCard> createCard(PokeCard card);

    Mono<PokeDetailVO> getPokeDetail(String name);

    Mono<PokeSpeciesVO> getPokeSpecies(String name);
}
