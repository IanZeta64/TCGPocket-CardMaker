package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.*;

public record UtilCardRequest(
        String name,
        Boolean isOfficialPoke,
        String image,
        String imageLine,
        String backgroundImage,
        String ex3dEffect,
        BackgroundEffectEnum backgroundEffect,
        CategoryEffectEnum categoryEffect,
        String illustrator,
        Boolean isShiny,
        String booster,
        UtilCardTypeEnum utilType,
        String description,
        Boolean isPromo
){}
