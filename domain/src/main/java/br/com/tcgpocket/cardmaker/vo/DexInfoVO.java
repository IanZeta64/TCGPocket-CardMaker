package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DexInfoVO(
        @JsonProperty("genus") String text
) {
    public String getText(){
        return text;
    }
}

