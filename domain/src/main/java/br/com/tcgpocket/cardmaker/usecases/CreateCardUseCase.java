package br.com.tcgpocket.cardmaker.usecases;

import br.com.tcgpocket.cardmaker.dataprovider.PokeCardDataProvider;
import br.com.tcgpocket.cardmaker.exceptions.CardCreationException;
import br.com.tcgpocket.cardmaker.service.CardService;
import br.com.tcgpocket.cardmaker.vo.PokeCardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class CreateCardUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCardUseCase.class);
    private final PokeCardDataProvider pokeCardDataProvider;
    private final CardService service;

    public CreateCardUseCase(PokeCardDataProvider pokeCardDataProvider, CardService service) {
        this.pokeCardDataProvider = pokeCardDataProvider;
        this.service = service;
    }

    public Mono<PokeCardResponse> create(PokeCardVO request, String user) {
        return Mono.defer(() -> {
                            log.info("m=create, s=init, i=createPokeCard, cardName={}, creator={}", request.name(), user);
                    var model = request.isOfficialPoke()
                            ? service.getPokeInfo(user, request)
                            .flatMap(pokeInfo -> service.buildModelWithOfficialPoke(user, request, pokeInfo))
                            : service.buildModel(user, request);

                            return model
                                    .flatMap(pokeCardDataProvider::createCard)
                                    .switchIfEmpty(Mono.error(new CardCreationException("Not created PokeCard: " + request.name())))
                                    .flatMap(service::toResponse)
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .doOnNext(it ->
                                            log.info("m=create, s=finished, i=createPokeCard, cardId={}, cardName={}, creator={}", it.id(), it.name(), user))
                                    .doOnError( ex -> log.error("m=create, s=error, i=createPokeCard, ex={}, message={}, cardName={}, creator={}", ex.getClass(), ex.getMessage(), request.name(), user));
                        }
                )
                .subscribeOn(Schedulers.boundedElastic());
    }
}
