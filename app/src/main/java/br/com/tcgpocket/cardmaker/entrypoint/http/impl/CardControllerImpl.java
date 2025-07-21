package br.com.tcgpocket.cardmaker.entrypoint.http.impl;

import br.com.tcgpocket.cardmaker.entrypoint.http.CardController;
import br.com.tcgpocket.cardmaker.usecases.CreateCardUseCase;
import br.com.tcgpocket.cardmaker.usecases.SearchCardUseCase;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.Map;


@RestController
public class CardControllerImpl implements CardController {
    private static final Logger log = LoggerFactory.getLogger(CardControllerImpl.class);
    private final CreateCardUseCase createCardUseCase;
    private final SearchCardUseCase searchCardUseCase;

    public CardControllerImpl(CreateCardUseCase createCardUseCase, SearchCardUseCase searchCardUseCase) {
        this.createCardUseCase = createCardUseCase;
        this.searchCardUseCase = searchCardUseCase;
    }

    @Override
    public Mono<CardResponse> createPokeCard(String user, PokeCardVO cardVO) {
        return createCardUseCase.create(cardVO, user)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error creating PokeCard - {} - user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Create PokeCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Flux<CardResponse> searchCard(String user, Map<String, String> filters) {
        return searchCardUseCase.search(user, filters)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error searching Cards - {} - user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Search Cards successfully - user={}", user));
    }
}
