package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EvolveFromVO(
        @JsonProperty("name") String name
) {
}
