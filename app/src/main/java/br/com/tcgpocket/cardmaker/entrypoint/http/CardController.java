package br.com.tcgpocket.cardmaker.entrypoint.http;

import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@RequestMapping("/card")
public interface CardController {

    @PostMapping("/create/poke")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CardResponse> createPokeCard(@RequestHeader("X-user") String user, @RequestBody PokeCardVO cardVO);
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    Flux<CardResponse> searchCard(@RequestHeader("X-user") String user,
                                  @RequestParam(required = false) Map<String, String> filters
                                      );
}
