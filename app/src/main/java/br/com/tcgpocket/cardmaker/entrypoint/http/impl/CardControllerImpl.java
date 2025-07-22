package br.com.tcgpocket.cardmaker.entrypoint.http.impl;

import br.com.tcgpocket.cardmaker.entrypoint.http.CardController;
import br.com.tcgpocket.cardmaker.usecases.*;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.ImageChangeRequest;
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
    private final UpdatePokeCardUseCase updatePokeCardUseCase;
    private final UpdateUtilCardUseCase updateUtilCardUseCase;
    private final ChangeImageCardUseCase changeImageCardUseCase;
    private final PromoteCardUseCase promoteCardUseCase;

    public CardControllerImpl(CreatePokeCardUseCase createPokeCardUseCase, CreateUtilCardUseCase createUtilCardUseCase, SearchCardUseCase searchCardUseCase, GetCardUseCase getCardUseCase, UpdatePokeCardUseCase updatePokeCardUseCase, UpdateUtilCardUseCase updateUtilCardUseCase, ChangeImageCardUseCase changeImageCardUseCase, PromoteCardUseCase promoteCardUseCase) {
        this.createPokeCardUseCase = createPokeCardUseCase;
        this.createUtilCardUseCase = createUtilCardUseCase;
        this.searchCardUseCase = searchCardUseCase;
        this.getCardUseCase = getCardUseCase;
        this.updatePokeCardUseCase = updatePokeCardUseCase;
        this.updateUtilCardUseCase = updateUtilCardUseCase;
        this.changeImageCardUseCase = changeImageCardUseCase;
        this.promoteCardUseCase = promoteCardUseCase;
    }

    @Override
    public Mono<CardResponse> createPokeCard(String user, PokeCardRequest card) {
        return createPokeCardUseCase.execute(user, card)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error creating PokeCard - e={}, user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Create PokeCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Mono<CardResponse> createUtilCard(String user, UtilCardRequest card) {
        return createUtilCardUseCase.execute(user, card)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error creating UtilCard - e={}, user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Create UtilCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Flux<CardResponse> searchCard(String user, Map<String, String> filters) {
        return searchCardUseCase.execute(user, filters)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error searching Cards - e={}, user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Search Cards successfully - user={}", user));
    }

    @Override
    public Mono<CardResponse> getById(String user, String id) {
        return getCardUseCase.execute(user, id)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error get Card by ID {} - e={}, user={}", id, err.getMessage(), user))
                .doOnNext(it -> log.info("Search Cards successfully {} - user={}", id, user));
    }

    @Override
    public Mono<CardResponse> updatePokeCard(String user, String id, PokeCardRequest card) {
        return updatePokeCardUseCase.execute(user, id, card)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error updating PokeCard {} - e={}, user={}", id, err.getMessage(), user))
                .doOnNext(it -> log.info("Update PokeCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Mono<CardResponse> updateUtilCard(String user, String id, UtilCardRequest card) {
        return updateUtilCardUseCase.execute(user, id, card)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error updating UtilCard {} - e={}, user={}", id, err.getMessage(), user))
                .doOnNext(it -> log.info("Update UtilCard successfully - {} - user={}", it.id(), user));
    }

    @Override
    public Mono<CardResponse> changeImage(String user, String id, ImageChangeRequest request) {
        return changeImageCardUseCase.execute(user, id, request)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error changing Card image {} - e={}, user={}", id, err.getMessage(), user))
                .doOnNext(it -> log.info("Change Card image successfully - {} - user={}", it.id(), user));
    }


    @Override
    public Mono<CardResponse> promote(String user, String id, boolean promotion) {
        return promoteCardUseCase.execute(user, id, promotion)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error promoting Card {} - e={}, user={}", id, err.getMessage(), user))
                .doOnNext(it -> log.info("Promote Card successfully - {} - user={}", it.id(), user));
    }
}
