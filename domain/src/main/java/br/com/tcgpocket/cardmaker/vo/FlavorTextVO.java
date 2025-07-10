package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlavorTextVO(
        @JsonProperty("flavor_text")
        String text
) {

    public String getText(){
        return text.replace("\n", " ").replace("\f", " ");
    }
}
