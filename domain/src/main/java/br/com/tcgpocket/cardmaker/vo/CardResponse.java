package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.model.Ability;
import br.com.tcgpocket.cardmaker.model.Visual;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardResponse(
    String id,
    String name,
    Visual visual,
    String createdBy,
    String illustrator,
    RarityEnum rarity,
    String booster,
    PromoteStatusEnum status,

    // CAMPOS DE POKE CARD
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

   // CAMPOS DE UTIL CARD
    UtilCardTypeEnum utilType,
    String description){

    public CardResponse(String id, String name, Visual visual, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, String specie, BattleCategoryEnum category, PokeTypeEnum type, EvolutionStageEnum evolutionStage, Integer dexNumber, String dexInfo, String pokeDescription, Integer ps, Ability ability, Ability attack, PokeTypeEnum weakness, Integer retreat, String evolveFrom, String evolveFromSprite) {
        this(id, name, visual, createdBy, illustrator, rarity, booster, status, specie, category, type, evolutionStage, dexNumber, dexInfo, pokeDescription, ps, ability, attack, weakness, retreat, evolveFrom, evolveFromSprite, null, null);
    }
    public CardResponse(String id, String name, Visual visual, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, UtilCardTypeEnum utilType, String description) {
        this(id, name, visual, createdBy, illustrator, rarity, booster, status, null, null, null, null, null, null, null, null, null, null, null, null, null, null, utilType, description);
    }
}

