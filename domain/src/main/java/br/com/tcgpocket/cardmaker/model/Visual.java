package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.BackgroundEffectEnum;
import br.com.tcgpocket.cardmaker.enums.CategoryEffectEnum;

public class Visual {
    private String image;
    private String imageLine;
    private String backgroundImage;
    private BackgroundEffectEnum backgroundEffectEnum;
    private String ex3dEffect;
    private CategoryEffectEnum categoryEffect;

    public Visual(String image, String imageLine, String backgroundImage, BackgroundEffectEnum backgroundEffectEnum, String ex3dEffect, CategoryEffectEnum categoryEffect) {
        this.image = image;
        this.imageLine = imageLine;
        this.backgroundImage = backgroundImage;
        this.backgroundEffectEnum = backgroundEffectEnum;
        this.ex3dEffect = ex3dEffect;
        this.categoryEffect = categoryEffect;
    }

    public String getImage() {
        return image;
    }

    public String getImageLine() {
        return imageLine;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public BackgroundEffectEnum getBackgroundEffectEnum() {
        return backgroundEffectEnum;
    }

    public String getEx3dEffect() {
        return ex3dEffect;
    }

    public CategoryEffectEnum getCategoryEffect() {
        return categoryEffect;
    }
}
