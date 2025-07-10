package br.com.tcgpocket.cardmaker.vo;

public record PokeInfoVO (

        String defaultImage,

        String shinyImage,
        String evolveFrom,
        String evolveFromSprite,

        Integer dexNumber,
        String dexInfo,

        String height,

        String weight,

        String dexDescription


){}
