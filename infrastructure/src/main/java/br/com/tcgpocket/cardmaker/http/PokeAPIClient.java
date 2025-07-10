package br.com.tcgpocket.cardmaker.http;

import br.com.tcgpocket.cardmaker.vo.PokeDetailVO;
import br.com.tcgpocket.cardmaker.vo.PokeSpeciesVO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PokeAPIClient {
    private static final String POKE_API_EXTERNAL_URL = "https://pokeapi.co/api/v2/";
    private final WebClient client;

    public PokeAPIClient(WebClient.Builder builder
    ) {
        this.client = builder.baseUrl(POKE_API_EXTERNAL_URL).build();
    }

    public Mono<PokeDetailVO> getInfoByName(String name){
        return this.client.get().uri("pokemon/" + name)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful()){
                        return result.bodyToMono(PokeDetailVO.class);
                    }else {
                        return Mono.empty();
                    }
                }).subscribeOn((Schedulers.boundedElastic()));
    }

    public Mono<PokeSpeciesVO> getSpecieByName(String name){
        return this.client.get().uri("pokemon-species/" + name)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful()){
                        return result.bodyToMono(PokeSpeciesVO.class);
                    }else {
                        return Mono.empty();
                    }
                }).subscribeOn((Schedulers.boundedElastic()));
    }
}