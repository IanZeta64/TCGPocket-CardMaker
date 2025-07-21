package br.com.tcgpocket.cardmaker.exceptions;

public class UnprocessedCardException extends RuntimeException {
    public UnprocessedCardException(String message) {
        super(message);
    }
}
