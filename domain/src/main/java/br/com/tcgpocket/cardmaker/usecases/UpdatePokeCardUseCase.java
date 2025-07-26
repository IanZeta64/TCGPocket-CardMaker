package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardNotFoundException;
import br.com.tcgpocket.cardmaker.exceptions.NotUpdateCardException;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.utils.Mapper;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class UpdatePokeCardUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdatePokeCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public UpdatePokeCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, String id, PokeCardRequest request) {
        return Mono.defer(() -> {
                    log.info("m=update, s=init, i=updatePokeCard, user={}, id={}, cardName={}", user, id, request.name());
                    var model = cardDataProvider.getById(id).ofType(PokeCard.class)
                            .switchIfEmpty(Mono.error(new CardNotFoundException("Not found PokeCard with id: " + id)))
                            .flatMap(card -> service.updateModel(user, request, card));

                    return model.flatMap(cardDataProvider::save).ofType(PokeCard.class)
                            .switchIfEmpty(Mono.error(new NotUpdateCardException("PokeCard not updated: " + id)))
                            .map(Mapper::toResponse);
                })
                .doOnNext(it ->
                        log.info("m=update, s=finished, i=updatePokeCard, user={}, id={}, cardName={}", user, it.id(), it.name()))
                .doOnError( ex -> log.error("m=update, s=error, i=updatePokeCard, ex={}, message={}, user={}, id={}, cardName={}", ex.getClass(), ex.getMessage(), user, id, request.name()))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
