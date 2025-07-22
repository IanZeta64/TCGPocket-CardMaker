package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardNotFoundException;
import br.com.tcgpocket.cardmaker.exceptions.NotUpdateCardException;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.utils.Mapper;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.ImageChangeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class ChangeImageCardUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChangeImageCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public ChangeImageCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, String id, ImageChangeRequest request) {
        return Mono.defer(() -> {
                    log.info("m=changeImage, s=init, i=changeCardImage, user={}, id={}", user, id);
                    return cardDataProvider.existsById(id)
                            .flatMap(exists -> (Boolean.FALSE.equals((exists)))
                                    ?
                                    Mono.error(new CardNotFoundException("Not found Card with id: " + id))
                                    :
                                    cardDataProvider.getById(id).map(it -> service.changeImage(it, request))
                                            .flatMap(cardDataProvider::save)
                                            .switchIfEmpty(Mono.error(new NotUpdateCardException("Card image not changed: " + id)))
                                            .map(Mapper::toResponse)
                            );
                })
                .doOnNext(it ->
                        log.info("m=changeImage, s=finished, i=changeCardImage, user={}, cardId={}, cardName={}", user, it.id(), it.name()))
                .doOnError(ex -> log.error("m=update, s=error, i=changeCardImage, ex={}, message={}, user={}, id={}", ex.getClass(), ex.getMessage(), user, id))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
