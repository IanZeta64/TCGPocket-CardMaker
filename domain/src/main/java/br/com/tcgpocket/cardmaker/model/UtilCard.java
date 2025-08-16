package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.*;
import org.springframework.data.annotation.TypeAlias;
import java.time.LocalDateTime;

@TypeAlias("util")
public class UtilCard extends Card{
    private UtilCardTypeEnum utilType;
    private String description;

    public UtilCard() {
    }

    public UtilCard(String name, String image, String imageLine, String backgroundImage, BackgroundEffectEnum backgroundEffect, String ex3dEffect, CategoryEffectEnum categoryEffect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, UtilCardTypeEnum utilType, String description) {
        super(name, image, imageLine, backgroundImage, backgroundEffect, ex3dEffect, categoryEffect, createdBy, illustrator, rarity, booster, status);
        this.utilType = utilType;
        this.description = description;
    }

    public UtilCard(String id, String name,String image, String imageLine, String backgroundImage, BackgroundEffectEnum backgroundEffect, String ex3dEffect, CategoryEffectEnum categoryEffect, String createdBy, String illustrator, RarityEnum rarity, String booster, PromoteStatusEnum status, LocalDateTime createdAt, UtilCardTypeEnum utilType, String description) {
        super(id, name, image, imageLine, backgroundImage, backgroundEffect, ex3dEffect, categoryEffect, createdBy, illustrator, rarity, booster, status, createdAt);
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
