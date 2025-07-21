package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.*;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("poke")
public class PokeCard extends Card {
    private String specie;
    private BattleCategoryEnum category;
    private PokeTypeEnum type;
    private EvolutionStageEnum evolutionStage;
    private Integer dexNumber;
    private String dexInfo;
    private String pokeDescription;
    private Integer ps;
    private Ability ability;
    private Ability attack;
    private PokeTypeEnum weakness;
    private Integer retreat;
    private String evolveFrom;
    private String evolveFromSprite;

    public PokeCard() {
    }

    public PokeCard(String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, String specie, BattleCategoryEnum category, PokeTypeEnum type, EvolutionStageEnum evolutionStage, Integer dexNumber, String dexInfo, String pokeDescription, Integer ps, Ability ability, Ability attack, PokeTypeEnum weakness, Integer retreat, String evolveFrom, String evolveFromSprite) {
        super(name, image, background, effect, createdBy, illustrator, rarity, booster, status);
        this.specie = specie;
        this.category = category;
        this.type = type;
        this.evolutionStage = evolutionStage;
        this.dexNumber = dexNumber;
        this.dexInfo = dexInfo;
        this.pokeDescription = pokeDescription;
        this.ps = ps;
        this.ability = ability;
        this.attack = attack;
        this.weakness = weakness;
        this.retreat = retreat;
        this.evolveFrom = evolveFrom;
        this.evolveFromSprite = evolveFromSprite;
    }

    public PokeCard(String id, String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, String specie, BattleCategoryEnum category, PokeTypeEnum type, EvolutionStageEnum evolutionStage, Integer dexNumber, String dexInfo, String pokeDescription, Integer ps, Ability ability, Ability attack, PokeTypeEnum weakness, Integer retreat, String evolveFrom, String evolveFromSprite) {
        super(id, name, image, background, effect, createdBy, illustrator, rarity, booster, status);
        this.specie = specie;
        this.category = category;
        this.type = type;
        this.evolutionStage = evolutionStage;
        this.dexNumber = dexNumber;
        this.dexInfo = dexInfo;
        this.pokeDescription = pokeDescription;
        this.ps = ps;
        this.ability = ability;
        this.attack = attack;
        this.weakness = weakness;
        this.retreat = retreat;
        this.evolveFrom = evolveFrom;
        this.evolveFromSprite = evolveFromSprite;
    }

    public String getSpecie() {
        return specie;
    }

    public BattleCategoryEnum getCategory() {
        return category;
    }

    public PokeTypeEnum getType() {
        return type;
    }

    public EvolutionStageEnum getEvolutionStage() {
        return evolutionStage;
    }

    public Integer getDexNumber() {
        return dexNumber;
    }

    public String getDexInfo() {
        return dexInfo;
    }

    public String getPokeDescription() {
        return pokeDescription;
    }

    public Integer getPs() {
        return ps;
    }

    public Ability getAbility() {
        return ability;
    }

    public Ability getAttack() {
        return attack;
    }

    public PokeTypeEnum getWeakness() {
        return weakness;
    }

    public Integer getRetreat() {
        return retreat;
    }

    public String getEvolveFrom() {
        return evolveFrom;
    }

    public String getEvolveFromSprite() {
        return evolveFromSprite;
    }


}