package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfficialArtWorkVO(
        @JsonProperty("front_default")
        String defaultImage,
        @JsonProperty("front_shiny")
        String shinyImage
) {
}
