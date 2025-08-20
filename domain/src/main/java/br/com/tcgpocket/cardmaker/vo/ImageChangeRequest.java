package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.BackgroundEffectEnum;

public record ImageChangeRequest(
       String image,
       String imageLine,
       String backgroundImage,
       BackgroundEffectEnum backgroundEffect,
       String ex3dEffect
) {
}
