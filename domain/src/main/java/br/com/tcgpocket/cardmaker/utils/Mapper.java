package br.com.tcgpocket.cardmaker.utils;

import br.com.tcgpocket.cardmaker.model.Card;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.model.UtilCard;
import br.com.tcgpocket.cardmaker.vo.CardResponse;

public class Mapper {

    public static CardResponse toResponse(Card card) {
        return switch (card) {
            case PokeCard poke -> toResponse(poke);
            case UtilCard util -> toResponse(util);
            default -> throw new IllegalArgumentException("Tipo de Card desconhecido: " + card.getClass());
        };
    }

    private static CardResponse toResponse(PokeCard card){
        return new CardResponse(
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
                card.getEvolveFrom(),
                card.getEvolveFromSprite()
        );
    }

    private static CardResponse toResponse(UtilCard card) {
        return new CardResponse(
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
                card.getUtilType(),
                card.getDescription()
        );
    }
}
