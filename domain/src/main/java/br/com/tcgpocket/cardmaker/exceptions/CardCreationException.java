package br.com.tcgpocket.cardmaker.exceptions;

public class CardCreationException extends RuntimeException{
    public CardCreationException(String message) {
        super((message));
    }
}
