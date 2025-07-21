package br.com.tcgpocket.cardmaker.handlerAdvice;

import br.com.tcgpocket.cardmaker.exceptions.CardCreationException;
import br.com.tcgpocket.cardmaker.exceptions.PokeNotFoundException;
import br.com.tcgpocket.cardmaker.handlerAdvice.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class AdviceHandler {

    private ResponseEntity<Object> handlePokemonException(HttpStatus status, List<String> errorMessages) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.getReasonPhrase(), errorMessages);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(PokeNotFoundException.class)
    protected ResponseEntity<Object> handlePokeNotFoundException(PokeNotFoundException ex) {
        return handlePokemonException(HttpStatus.NOT_FOUND, List.of(ex.getMessage()));
    }

    @ExceptionHandler(CardCreationException.class)
    protected ResponseEntity<Object> handleCardCreationException(CardCreationException ex) {
        return handlePokemonException(HttpStatus.UNPROCESSABLE_ENTITY, List.of(ex.getMessage()));
    }
}
