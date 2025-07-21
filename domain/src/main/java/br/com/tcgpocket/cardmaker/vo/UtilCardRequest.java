package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.*;

public record UtilCardRequest(
        String name,
        Boolean isOfficialPoke,
        String image,
        String illustrator,
        Boolean isShiny,
        BackgroundEnum background,
        EffectEnum effect,
        String booster,
        UtilCardTypeEnum utilType,
        String description,
        Boolean isPromo
){}
