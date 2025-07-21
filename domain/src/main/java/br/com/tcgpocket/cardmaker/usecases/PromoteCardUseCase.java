package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardNotFoundException;
import br.com.tcgpocket.cardmaker.exceptions.NotUpdateCardException;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.utils.Mapper;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PromoteCardUseCase {
    private static final Logger log = LoggerFactory.getLogger(PromoteCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public PromoteCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, String id, boolean promotion) {
        return Mono.defer(() -> {
                    log.info("m=promote, s=init, i=promoteCard, user={}, id={}", user, id);
                    var model = cardDataProvider.existsById(id)
                            .flatMap(exists -> Boolean.TRUE.equals((exists))
                                    ? cardDataProvider.getById(id).map(it -> service.promote(it, promotion))
                                    : Mono.error(new CardNotFoundException("Not found Card with id: " + id)));

                    return model.flatMap(cardDataProvider::save)
                            .switchIfEmpty(Mono.error(new NotUpdateCardException("Card not promoted: " + id)))
                            .map(Mapper::toResponse);
                })
                .doOnNext(it ->
                        log.info("m=promte, s=finished, i=promoteCard, user={}, cardId={}, cardName={}", user, it.id(), it.name()))
                .doOnError( ex -> log.error("m=update, s=error, i=changeCardImage, ex={}, message={}, user={}, id={}", ex.getClass(), ex.getMessage(), user, id))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
