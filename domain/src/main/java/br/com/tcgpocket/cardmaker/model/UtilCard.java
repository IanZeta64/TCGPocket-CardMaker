package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.*;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("util")
public class UtilCard extends Card{
    private UtilCardTypeEnum utilType;
    private String description;

    public UtilCard() {
    }

    public UtilCard(String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, UtilCardTypeEnum utilType, String description) {
        super(name, image, background, effect, createdBy, illustrator, rarity, booster, status);
        this.utilType = utilType;
        this.description = description;
    }

    public UtilCard(String id, String name, String image, BackgroundEnum background, EffectEnum effect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, UtilCardTypeEnum utilType, String description) {
        super(id, name, image, background, effect, createdBy, illustrator, rarity, booster, status);
        this.utilType = utilType;
        this.description = description;
    }

    public UtilCardTypeEnum getUtilType() {
        return utilType;
    }

    public String getDescription() {
        return description;
    }
}
