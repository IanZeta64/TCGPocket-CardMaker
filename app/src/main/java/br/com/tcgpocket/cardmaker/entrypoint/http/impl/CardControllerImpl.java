package br.com.tcgpocket.cardmaker.entrypoint.http.impl;

import br.com.tcgpocket.cardmaker.entrypoint.http.CardController;
import br.com.tcgpocket.cardmaker.usecases.CreatePokeCardUseCase;
import br.com.tcgpocket.cardmaker.usecases.CreateUtilCardUseCase;
import br.com.tcgpocket.cardmaker.usecases.GetCardUseCase;
import br.com.tcgpocket.cardmaker.usecases.SearchCardUseCase;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardRequest;
import br.com.tcgpocket.cardmaker.vo.UtilCardRequest;
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
    private final CreatePokeCardUseCase createPokeCardUseCase;
    private final CreateUtilCardUseCase createUtilCardUseCase;
    private final SearchCardUseCase searchCardUseCase;
    private final GetCardUseCase getCardUseCase;

    public CardControllerImpl(CreatePokeCardUseCase createPokeCardUseCase, CreateUtilCardUseCase createUtilCardUseCase, SearchCardUseCase searchCardUseCase, GetCardUseCase getCardUseCase) {
        this.createPokeCardUseCase = createPokeCardUseCase;
        this.createUtilCardUseCase = createUtilCardUseCase;
        this.searchCardUseCase = searchCardUseCase;
        this.getCardUseCase = getCardUseCase;
    }

    @Override
    public Mono<CardResponse> createPokeCard(String user, PokeCardRequest card) {
        return createPokeCardUseCase.execute(user, card)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error creating PokeCard - {} - user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Create PokeCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Mono<CardResponse> createPokeCard(String user, UtilCardRequest card) {
        return createUtilCardUseCase.execute(user, card)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error creating UtilCard - {} - user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Create UtilCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Flux<CardResponse> searchCard(String user, Map<String, String> filters) {
        return searchCardUseCase.execute(user, filters)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error searching Cards - {} - user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Search Cards successfully - user={}", user));
    }

    @Override
    public Mono<CardResponse> getById(String user, String id) {
        return getCardUseCase.execute(user, id)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error get Card by ID {} - {} - user={}", id, err.getMessage(), user))
                .doOnNext(it -> log.info("Search Cards successfully {} - user={}", id, user));
    }
}
