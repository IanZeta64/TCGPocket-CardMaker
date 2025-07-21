package br.com.tcgpocket.cardmaker.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
