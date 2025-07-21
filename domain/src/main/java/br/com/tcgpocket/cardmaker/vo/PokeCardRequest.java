package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.model.Ability;

public record PokeCardRequest(
        String name,
        Boolean isOfficialPoke,
        String image,
        String illustrator,
        Boolean isShiny,
        BackgroundEnum background,
        EffectEnum effect,
        String booster,
        String specie,
        BattleCategoryEnum category,
        PokeTypeEnum type,
        Integer dexNumber,
        String dexInfo,
        String pokeDescription,
        EvolutionStageEnum evolutionStage,
        Boolean isFossil,
        String evolveFrom,
        String evolveFromSprite,
        Integer ps,
        Ability ability,
        Ability attack,
        PokeTypeEnum weakness,
        Integer retreat,
        PromoteStatusEnum status,
        Boolean isPromo
) {
}
