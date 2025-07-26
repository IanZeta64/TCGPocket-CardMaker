package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardCreationException;
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
public class CreateUtilCardUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateUtilCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public CreateUtilCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, UtilCardRequest request) {
        return Mono.defer(() -> {
                    log.info("m=create, s=init, i=createUtilCard, cardName={}, creator={}", request.name(), user);
                    return service.buildModel(user, request)
                            .flatMap(cardDataProvider::save).ofType(UtilCard.class)
                            .switchIfEmpty(Mono.error(new CardCreationException("Not created UtilCard: " + request.name())))
                            .map(Mapper::toResponse);
                }
        )
                            .doOnNext(it ->
                                    log.info("m=create, s=finished, i=createUtilCard, cardId={}, cardName={}, creator={}", it.id(), it.name(), user))
                            .doOnError(ex -> log.error("m=create, s=error, i=createUtilCard, ex={}, message={}, cardName={}, creator={}", ex.getClass(), ex.getMessage(), request.name(), user))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
