package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.BackgroundEffectEnum;
import br.com.tcgpocket.cardmaker.enums.CategoryEffectEnum;
import br.com.tcgpocket.cardmaker.enums.PromoteStatusEnum;
import br.com.tcgpocket.cardmaker.enums.RarityEnum;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

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
    protected Visual visual;
    protected String createdBy;
    protected String illustrator;
    protected RarityEnum rarity;
    protected String booster;
    protected PromoteStatusEnum status;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    protected Card() {
    }

    protected Card(String name, String image, String imageLine, String backgroundImage, BackgroundEffectEnum backgroundEffectEnum, String ex3dEffect, CategoryEffectEnum categoryEffect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status) {
        this.name = name;
        this.visual = new Visual(image, imageLine, backgroundImage, backgroundEffectEnum, ex3dEffect, categoryEffect);
        this.createdBy = createdBy;
        this.illustrator = illustrator;
        this.rarity = rarity;
        this.booster = booster;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    protected Card(String id, String name, String image, String imageLine, String backgroundImage, BackgroundEffectEnum backgroundEffectEnum, String ex3dEffect, CategoryEffectEnum categoryEffect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.visual = new Visual(image, imageLine, backgroundImage, backgroundEffectEnum, ex3dEffect, categoryEffect);
        this.createdBy = createdBy;
        this.illustrator = illustrator;
        this.rarity = rarity;
        this.booster = booster;
        this.status = status;
        this.createdAt = createdAt;
        this. updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Visual getVisual() {
        return visual;
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

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

    public void setStatus(PromoteStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
