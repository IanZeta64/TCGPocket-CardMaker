package br.com.tcgpocket.cardmaker.entrypoint.http.impl;

import br.com.tcgpocket.cardmaker.entrypoint.http.CardController;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.usecases.CreateCardUseCase;
import br.com.tcgpocket.cardmaker.vo.PokeCardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@RestController
public class CardControllerImpl implements CardController {
    private static final Logger log = LoggerFactory.getLogger(CardControllerImpl.class);
    private final CreateCardUseCase createCardUseCase;

    public CardControllerImpl(CreateCardUseCase createCardUseCase) {
        this.createCardUseCase = createCardUseCase;
    }

    @Override
    public Mono<PokeCardResponse> createPokeCard(String user, PokeCardVO cardVO) {
        return createCardUseCase.create(cardVO, user)
                .subscribeOn(Schedulers.parallel())
                .doOnError(err -> log.error("Error creating PokeCard - {} - user={}", err.getMessage(), user))
                .doOnNext(it -> log.info("Create PokeCard Sucessfully - {} - user={}", it.id(), user));
    }
}
