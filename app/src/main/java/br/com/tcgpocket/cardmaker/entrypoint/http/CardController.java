package br.com.tcgpocket.cardmaker.entrypoint.http;

import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardRequest;
import br.com.tcgpocket.cardmaker.vo.UtilCardRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@RequestMapping("/card")
public interface CardController {

    @PostMapping("/create/poke")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CardResponse> createPokeCard(@RequestHeader("X-user") String user, @RequestBody PokeCardRequest card);

    @PostMapping("/create/util")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CardResponse> createUtilCard(@RequestHeader("X-user") String user, @RequestBody UtilCardRequest card);

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    Flux<CardResponse> searchCard(@RequestHeader("X-user") String user,
                                  @RequestParam(required = false) Map<String, String> filters
                                      );
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<CardResponse> getById(@RequestHeader("X-user") String user,
                               @PathVariable(required = false) String id
    );

    @PutMapping("/update/poke/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<CardResponse> updatePokeCard(@RequestHeader("X-user") String user,
                                      @PathVariable String id,
                                      @RequestBody PokeCardRequest card);

    @PutMapping("/update/util/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<CardResponse> updateUtilCard(@RequestHeader("X-user") String user,
                                      @PathVariable String id,
                                      @RequestBody UtilCardRequest card);
}
