package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.BackgroundEnum;
import br.com.tcgpocket.cardmaker.enums.EffectEnum;
import br.com.tcgpocket.cardmaker.enums.PromoteStatusEnum;
import br.com.tcgpocket.cardmaker.enums.RarityEnum;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "cardType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PokeCard.class, name = "POKEMON"),
        @JsonSubTypes.Type(value = UtilCard.class, name = "UTIL")
})

@Document("cards")
public abstract class Card {
    @Id
    protected String id;
    protected String name;
    protected String image;
    protected BackgroundEnum background;
    protected EffectEnum effect;
    protected String createdBy;
    protected String illustrator;
    protected RarityEnum rarity;
    protected String booster;
    protected PromoteStatusEnum status;

    protected Card() {
    }

    protected Card(String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status) {
        this.name = name;
        this.image = image;
        this.background = background;
        this.effect = effect;
        this.createdBy = createdBy;
        this.illustrator = illustrator;
        this.rarity = rarity;
        this.booster = booster;
        this.status = status;
    }

    protected Card(String id, String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator,RarityEnum rarity, String booster, PromoteStatusEnum status) {
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

    public RarityEnum getRarity() {
        return rarity;
    }

    public String getBooster() {
        return booster;
    }

    public PromoteStatusEnum getStatus() {
        return status;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public void setBackground(BackgroundEnum background) {
        this.background = background;
    }
    public void setStatus(PromoteStatusEnum status) {
        this.status = status;
    }
}
