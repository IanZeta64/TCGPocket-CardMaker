package br.com.tcgpocket.cardmaker.repository.impl;

import br.com.tcgpocket.cardmaker.dataprovider.PokeCardDataProvider;
import br.com.tcgpocket.cardmaker.http.PokeAPIClient;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.repository.PokeCardRepository;
import br.com.tcgpocket.cardmaker.repository.document.PokeCardDocument;
import br.com.tcgpocket.cardmaker.vo.PokeDetailVO;
import br.com.tcgpocket.cardmaker.vo.PokeSpeciesVO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PokeCardDataProviderImpl implements PokeCardDataProvider {
    private final PokeCardRepository repository;
    private final PokeAPIClient client;

    public PokeCardDataProviderImpl(PokeCardRepository repository, PokeAPIClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public Mono<PokeCard> createCard(PokeCard card) {
        return repository.save(PokeCardDocument.fromDomain(card))
                .map(PokeCardDocument::toDomain);
    }

    @Override
    public Mono<PokeDetailVO> getPokeDetail(String name) {
        return client.getInfoByName(name);
    }

    @Override
    public Mono<PokeSpeciesVO> getPokeSpecies(String name) {
        return client.getSpecieByName(name);
    }
}
