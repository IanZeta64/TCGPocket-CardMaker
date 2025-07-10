package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PokeDetailVO
        (@JsonProperty("sprites")
         SpriteVO image,
         @JsonProperty("id")
         Integer dexNumber,
         @JsonProperty("height")
         String height,
         @JsonProperty("weight")
         String weight) {

    public String getDefaultImage() {
        return image.other().officialArtWork().defaultImage();
    }

    public String getShinyImage() {
        return image.other().officialArtWork().shinyImage();
    }

    public String getSprite() {
       return image.sprite();
    }
}

