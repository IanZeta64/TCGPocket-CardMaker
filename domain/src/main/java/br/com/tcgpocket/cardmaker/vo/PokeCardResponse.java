package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.model.Ability;
import br.com.tcgpocket.cardmaker.model.PokeCard;

public record PokeCardResponse(
        String id,
        String name,
        String image,
        BackgroundEnum background,
        EffectEnum effect,
        String createdBy,
        String illustrator,
        RarityEnum rarity,
        String booster,

        String specie,
        BattleCategoryEnum category,
        PokeTypeEnum type,
        EvolutionStageEnum evolutionStage,
        Integer dexNumber,
        String dexInfo,
        String pokeDescription,
        Integer ps,
        Ability ability,
        Ability attack,
        PokeTypeEnum weakness,
        Integer retreat,
        String evolveFrom,
        String evolveFromSprite,
        PromoteStatusEnum status
        ) {
}
