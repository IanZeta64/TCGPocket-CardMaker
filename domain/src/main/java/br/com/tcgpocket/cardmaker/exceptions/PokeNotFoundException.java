package br.com.tcgpocket.cardmaker.exceptions;

public class PokeNotFoundException extends RuntimeException {
    public PokeNotFoundException(String message) {
        super(message);
    }
}
