package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpriteVO
        (@JsonProperty("other")
         OtherImageVO other,
         @JsonProperty("front_default")
         String sprite) {
}

