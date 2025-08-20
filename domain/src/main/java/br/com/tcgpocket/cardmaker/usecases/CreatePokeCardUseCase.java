package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardCreationException;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.utils.Mapper;
import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class CreatePokeCardUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreatePokeCardUseCase.class);
    private final CardDataProvider cardDataProvider;
    private final CardService service;

    public CreatePokeCardUseCase(CardDataProvider cardDataProvider, CardService service) {
        this.cardDataProvider = cardDataProvider;
        this.service = service;
    }

    public Mono<CardResponse> execute(String user, PokeCardRequest request) {
        return Mono.defer(() -> {
                            log.info("m=create, s=init, i=createPokeCard, cardName={}, creator={}", request.name(), user);
                            var model = Boolean.TRUE.equals(request.isOfficialPoke())
                                    ? service.getPokeInfoFromCache(request.name()).
                                    switchIfEmpty(service.getPokeInfoFromExternalSource(user, request)
                                            .flatMap(pokeInfo ->
                                                    service.savePokeInfoInCache(request.name(), pokeInfo)
                                                            .thenReturn(pokeInfo)
                                            )
                                    )
                                    .flatMap(pokeInfo -> service.buildModelWithOfficialPoke(user, request, pokeInfo))
                                    : service.buildModel(user, request);

                            return model
                                    .flatMap(cardDataProvider::save).ofType(PokeCard.class)
                                    .switchIfEmpty(Mono.error(new CardCreationException("Not created PokeCard: " + request.name())))
                                    .map(Mapper::toResponse);
                        }
                )
                .doOnNext(it ->
                        log.info("m=create, s=finished, i=createPokeCard, cardId={}, cardName={}, creator={}", it.id(), it.name(), user))
                .doOnError(ex -> log.error("m=create, s=error, i=createPokeCard, ex={}, message={}, cardName={}, creator={}", ex.getClass(), ex.getMessage(), request.name(), user))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
