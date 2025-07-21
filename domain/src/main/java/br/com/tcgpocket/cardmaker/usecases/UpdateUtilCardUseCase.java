package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardNotFoundException;
import br.com.tcgpocket.cardmaker.exceptions.NotUpdateCardException;
import br.com.tcgpocket.cardmaker.model.UtilCard;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.utils.Mapper;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.UtilCardRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class UpdateUtilCardUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateUtilCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public UpdateUtilCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, String id, UtilCardRequest request) {
        return Mono.defer(() -> {
                    log.info("m=update, s=init, i=updateUtilCard, user={}, id={}, cardName={}", user, id, request.name());
                    var model = cardDataProvider.existsById(id)
                            .flatMap(exists -> Boolean.TRUE.equals((exists))
                                    ? service.buildModel(user, request, id)
                                    : Mono.error(new CardNotFoundException("Not found UtilCard with id: " + id)));

                    return model.flatMap(cardDataProvider::save).ofType(UtilCard.class)
                            .switchIfEmpty(Mono.error(new NotUpdateCardException("UtilCard not updated: " + id)))
                            .map(Mapper::toResponse);
                })
                .doOnNext(it ->
                        log.info("m=update, s=finished, i=updateUtilCard, user={}, cardId={}, cardName={}", user, it.id(), it.name()))
                .doOnError( ex -> log.error("m=update, s=error, i=updateUtilCard, ex={}, message={}, user={}, id={}, cardName={}", ex.getClass(), ex.getMessage(), user, id, request.name()))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
