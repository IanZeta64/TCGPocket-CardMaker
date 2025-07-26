package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class DeleteCardUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteCardUseCase.class);
    private final CardDataProvider cardDataProvider;

    public DeleteCardUseCase(CardDataProvider cardDataProvider) {
        this.cardDataProvider = cardDataProvider;
    }

    public Mono<Void> execute(String user, String id) {
        return Mono.defer(() -> {
                    log.info("m=delete, s=init, i=deleteById, user={}, id={}", user, id);
                    return cardDataProvider.existsById(id)
                            .flatMap(exists -> Boolean.FALSE.equals((exists))
                                    ? Mono.error(new CardNotFoundException("Not found PokeCard with id: " + id))
                                    : cardDataProvider.delete(id));
                })
                .doOnNext(it ->
                        log.info("m=delete, s=finished, i=deleteById, user={}, id={}", id, user))
                .doOnError(ex -> log.error("m=delete, s=error, i=deleteById, user={}, id={}, ex={}, message={}", user, id, ex.getClass(), ex.getMessage()))
                .subscribeOn(Schedulers.boundedElastic());
    }
}

