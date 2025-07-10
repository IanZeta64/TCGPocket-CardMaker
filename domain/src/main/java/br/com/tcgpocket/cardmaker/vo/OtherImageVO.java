package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtherImageVO(
        @JsonProperty("official-artwork")
        OfficialArtWorkVO officialArtWork) {
}
