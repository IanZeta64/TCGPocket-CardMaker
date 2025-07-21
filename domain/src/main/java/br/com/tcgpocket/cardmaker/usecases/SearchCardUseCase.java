package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import java.util.Map;

@Component
public class SearchCardUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public SearchCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Flux<CardResponse> search(String user, Map<String, String> filters) {
        return Flux.defer(() -> {
                    log.info("m=search, s=init, i=searchCard, user={}, filters={}", user, filters);
                    var query = service.getQuery(filters);
                    return cardDataProvider.search(query)
                            .flatMap(service::toResponse)
                            .doOnNext(it ->
                                    log.info("m=search, s=finished, i=searchCard, user={}, filters={}", user, filters))
                            .doOnError( ex -> log.error("m=search, s=error, i=searchCard, ex={}, message={}, user{}", ex.getClass(), ex.getMessage(), user));
                }
        )
                .subscribeOn(Schedulers.boundedElastic());
    }
}
