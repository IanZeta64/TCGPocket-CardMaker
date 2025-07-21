package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardCreationException;
import br.com.tcgpocket.cardmaker.exceptions.CardNotFoundException;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class GetCardUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public GetCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, String id) {
        return Mono.defer(() -> {
            log.info("m=get, s=init, i=getById, user={}, id={}", user, id);
            return cardDataProvider.getById(id)
                    .switchIfEmpty(Mono.error(new CardNotFoundException("Not found Card with id: " + id)))
                    .flatMap(service::toResponse)
                    .doOnNext(it ->
                            log.info("m=get, s=finished, i=getById, user={}, cardId={}, cardName={}", it.id(), it.name(), user))
                    .doOnError(ex -> log.error("m=get, s=error, i=getById, user={} ex={}, message={}", user, ex.getClass(), ex.getMessage()));

        }).subscribeOn(Schedulers.boundedElastic());
    }
}
