package br.com.tcgpocket.cardmaker.handlerAdvice.response;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {
    private int status;
    private String error;
    private List<String> messages;
    private Instant instant;

    public ErrorResponse(int status, String error, List<String> messageList) {
        this.status = status;
        this.error = error;
        this.messages = messageList;
        this.instant = Instant.now();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Instant getInstant() {
        return instant;
    }
}