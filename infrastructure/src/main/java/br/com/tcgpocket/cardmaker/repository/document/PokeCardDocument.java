package br.com.tcgpocket.cardmaker.repository.document;

import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.model.Ability;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pokeCard")
public class PokeCardDocument {

    @Id
    private String id;
    private String name;
    private String image;
    private BackgroundEnum background;
    private EffectEnum effect;
    private String createdBy;
    private String illustrator;
    private RarityEnum rarity;
    private String booster;

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
    private PromoteStatusEnum status;

    public PokeCardDocument(String id, String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy,
                            String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, String specie,
                            BattleCategoryEnum category, PokeTypeEnum type, EvolutionStageEnum evolutionStage,
                            Integer dexNumber, String dexInfo, String pokeDescription, Integer ps,
                            Ability ability, Ability attack, PokeTypeEnum weakness, Integer retreat, String evolveFrom) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.background = background;
        this.effect = effect;
        this.createdBy = createdBy;
        this.illustrator = illustrator;
        this.rarity = rarity;
        this.booster = booster;
        this.status = status;
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
    }

    public static PokeCardDocument fromDomain(PokeCard card) {
        return new PokeCardDocument(
                card.getId(),
                card.getName(),
                card.getImage(),
                card.getBackground(),
                card.getEffect(),
                card.getCreatedBy(),
                card.getIllustrator(),
                card.getRarity(),
                card.getBooster(),
                card.getStatus(),
                card.getSpecie(),
                card.getCategory(),
                card.getType(),
                card.getEvolutionStage(),
                card.getDexNumber(),
                card.getDexInfo(),
                card.getPokeDescription(),
                card.getPs(),
                card.getAbility(),
                card.getAttack(),
                card.getWeakness(),
                card.getRetreat(),
                card.getEvolveFrom()
        );
    }

    public PokeCard toDomain() {
        return new PokeCard(
                id,
                name,
                image,
                background,
                effect,
                createdBy,
                illustrator,
                rarity,
                booster,
                status,
                specie,
                category,
                type,
                evolutionStage,
                dexNumber,
                dexInfo,
                pokeDescription,
                ps,
                ability,
                attack,
                weakness,
                retreat,
                evolveFrom,
                evolveFromSprite
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public BackgroundEnum getBackground() {
        return background;
    }

    public EffectEnum getEffect() {
        return effect;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getIllustrator() {return illustrator;}

    public RarityEnum getRarity() {
        return rarity;
    }

    public String getBooster() {return booster;}

    public PromoteStatusEnum getStatus() {
        return status;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBackground(BackgroundEnum background) {
        this.background = background;
    }

    public void setEffect(EffectEnum effect) {
        this.effect = effect;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

    public void setRarity(RarityEnum rarity) {
        this.rarity = rarity;
    }

    public void setBooster(String booster) {
        this.booster = booster;
    }

    public void setStatus(PromoteStatusEnum status) {
        this.status = status;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public void setCategory(BattleCategoryEnum category) {
        this.category = category;
    }

    public void setType(PokeTypeEnum type) {
        this.type = type;
    }

    public void setEvolutionStage(EvolutionStageEnum evolutionStage) {
        this.evolutionStage = evolutionStage;
    }

    public void setDexNumber(Integer dexNumber) {
        this.dexNumber = dexNumber;
    }

    public void setDexInfo(String dexInfo) {
        this.dexInfo = dexInfo;
    }

    public void setPokeDescription(String pokeDescription) {
        this.pokeDescription = pokeDescription;
    }

    public void setPs(Integer ps) {
        this.ps = ps;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public void setAttack(Ability attack) {
        this.attack = attack;
    }

    public void setWeakness(PokeTypeEnum weakness) {
        this.weakness = weakness;
    }

    public void setRetreat(Integer retreat) {
        this.retreat = retreat;
    }

    public void setEvolveFrom(String evolveFrom) {
        this.evolveFrom = evolveFrom;
    }

    public void setEvolveFromSprite(String evolveFromSprite) {
        this.evolveFromSprite = evolveFromSprite;
    }
}