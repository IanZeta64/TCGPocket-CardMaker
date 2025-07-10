package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.BackgroundEnum;
import br.com.tcgpocket.cardmaker.enums.EffectEnum;
import br.com.tcgpocket.cardmaker.enums.PromoteStatusEnum;
import br.com.tcgpocket.cardmaker.enums.RarityEnum;

public abstract class Card {
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

    public Card() {
    }

    public Card(String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status) {
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

    public Card(String id, String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator,RarityEnum rarity, String booster, PromoteStatusEnum status) {
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
}
