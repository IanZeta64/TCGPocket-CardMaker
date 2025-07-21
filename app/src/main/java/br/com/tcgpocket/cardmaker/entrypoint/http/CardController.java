package br.com.tcgpocket.cardmaker.entrypoint.http;

import br.com.tcgpocket.cardmaker.vo.PokeCardResponse;
import br.com.tcgpocket.cardmaker.vo.PokeCardVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;



@RequestMapping("/card")
public interface CardController {

    @PostMapping("/create/poke")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<PokeCardResponse> createPokeCard(@RequestHeader("X-user") String user, @RequestBody PokeCardVO cardVO);
}
