package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.model.Ability;
import br.com.tcgpocket.cardmaker.model.PokeCard;

public record PokeCardVO(
        String id,
        String name,
        Boolean isOfficialPoke,
        String image,
        String illustrator,
        Boolean isShiny,
        BackgroundEnum background,
        EffectEnum effect,
        RarityEnum rarity,
        String booster,
        String specie,
        BattleCategoryEnum category,
        PokeTypeEnum type,
        Integer dexNumber, // nullable
        String dexInfo, // nullable
        String pokeDescription, // nullable
        EvolutionStageEnum evolutionStage,
        Boolean isFossil,
        String evolveFrom, // nullable
        String evolveFromSprite,
        Integer ps,
        Ability ability,
        Ability attack,
        PokeTypeEnum weakness,
        Integer retreat,
        PromoteStatusEnum status,
        Boolean isPromo
) {

    public PokeCard toEntity(String user, String evolveFrom, String evolveFromSprite) {
        return new PokeCard(
                id, name, image, background, effect, user, illustrator, rarity, booster, PromoteStatusEnum.PRIVATE,
                specie, category, type, evolutionStage, dexNumber, dexInfo,
                pokeDescription, ps, ability, attack, weakness, retreat, evolveFrom, evolveFromSprite
        );
    }
}
