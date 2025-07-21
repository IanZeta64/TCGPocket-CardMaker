package br.com.tcgpocket.cardmaker.service;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.exceptions.PokeNotFoundException;
import br.com.tcgpocket.cardmaker.model.Card;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.model.UtilCard;
import br.com.tcgpocket.cardmaker.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
    private final CardDataProvider cardDataProvider;

    public CardService(CardDataProvider cardDataProvider) {
        this.cardDataProvider = cardDataProvider;
    }

    public Mono<PokeInfoVO> getPokeInfo(String user, PokeCardVO card) {
        return Mono.defer(() -> {
                            log.info("m=create, s=init, i=getPokeInfo, cardName={}, creator={}", card.name(), user);
                            return cardDataProvider.getPokeDetail(card.name())
                                    .switchIfEmpty(Mono.error(new PokeNotFoundException("Details not found of poke: " + card.name())))
                                    .zipWith(cardDataProvider.getPokeSpecies(card.name()))
                                    .switchIfEmpty(Mono.error(new PokeNotFoundException("Infos not found of poke: " + card.name())))
                                    .flatMap(tuple -> {
                                        var details = tuple.getT1();
                                        var species = tuple.getT2();

                                        var validateEvolutionAndNoFossilPoke = validateEvolutionAndNoFossilPoke(card, species);
                                        Mono<String> preEvolutionSprite = validateEvolutionAndNoFossilPoke
                                                ? cardDataProvider.getPokeDetail(species.evolveFrom().name()).map(PokeDetailVO::getSprite)
                                                .switchIfEmpty(Mono.error(new PokeNotFoundException("Details not found of poke: " + card.name())))
                                                : Mono.empty();

                                        return preEvolutionSprite
                                                .map(evolveFromSprite -> new PokeInfoVO(
                                                        details.getDefaultImage(),
                                                        details.getShinyImage(),
                                                        species.evolveFrom().name(),
                                                        evolveFromSprite,
                                                        details.dexNumber(),
                                                        species.getDexInfo(),
                                                        details.height(),
                                                        details.weight(),
                                                        species.getDexDescription()
                                                ))
                                                .switchIfEmpty(
                                                        Mono.just(new PokeInfoVO(
                                                                        details.getDefaultImage(),
                                                                        details.getShinyImage(),
                                                                        null,
                                                                        null,
                                                                        details.dexNumber(),
                                                                        species.getDexInfo(),
                                                                        details.height(),
                                                                        details.weight(),
                                                                        species.getDexDescription()
                                                                )
                                                        )
                                                );
                                    });
                        }

                )
                .doOnNext(it -> log.info("m=getPokeInfo, s=finished, i=getPokeInfo, cardName={}, creator={}", card.name(), user))
                .doOnError(ex -> log.error("m=getPokeInfo, s=error, i=getPokeInfo, ex={}, message={}, cardName={}, creator={}", ex.getClass(), ex.getMessage(), card.name(), user))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<PokeCard> buildModelWithOfficialPoke(String user, PokeCardVO card, PokeInfoVO pokeInfo) {
        boolean validateImage = card.image() != null;
        return Mono.just(new PokeCard(
                        card.category() != BattleCategoryEnum.EX ? card.name() : card.name() + "-EX",
                        validateImage ? card.image() : card.isShiny() ? pokeInfo.shinyImage() : pokeInfo.defaultImage(),
                        card.background(),
                        validateEffect(card),
                        user,
                        validateImage ? card.illustrator() : null,
                        Boolean.TRUE.equals(card.isPromo()) ? RarityEnum.PROMO : validateRarity(card),
                        card.booster(),
                        PromoteStatusEnum.PRIVATE,
                        card.name(),
                        card.category(),
                        card.type(),
                        card.evolutionStage(),
                        pokeInfo.dexNumber(),
                        String.format("Nº%04d %s Height: %s ft Weight: %s lb.", pokeInfo.dexNumber(), pokeInfo.dexInfo(), pokeInfo.height(), pokeInfo.height()),
                        pokeInfo.dexDescription().replace("\\n", " ").replace("\f", " "),
                        card.ps(),
                        card.ability(),
                        card.attack(),
                        card.weakness(),
                        card.retreat(),
                        pokeInfo.evolveFrom(),
                        pokeInfo.evolveFromSprite()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<PokeCard> buildModel(String user, PokeCardVO card) {
        return Mono.just(new PokeCard(
                        card.category() != BattleCategoryEnum.EX ? card.name() : card.name() + "-EX",
                        card.image(),
                        card.background(),
                        validateEffect(card),
                        user,
                        card.illustrator(),
                        card.isPromo() ? RarityEnum.PROMO : validateRarity(card),
                        card.booster(),
                        PromoteStatusEnum.PRIVATE,
                        card.name(),
                        card.category(),
                        card.type(),
                        card.evolutionStage(),
                        card.dexNumber(),
                        String.format("Nº %s %s.", card.dexNumber(), card.dexInfo()),
                        card.pokeDescription(),
                        card.ps(),
                        card.ability(),
                        card.attack(),
                        card.weakness(),
                        card.retreat(),
                        card.evolveFrom(),
                        card.evolveFromSprite()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private RarityEnum validateRarity(PokeCardVO card) {
        if (Boolean.TRUE.equals(card.isPromo())) return RarityEnum.PROMO;
        RarityEnum rarity = null;
        if (card.category() == BattleCategoryEnum.NO_EX) {
            rarity = switch (card.effect()) {
                case COMMON ->
                        card.evolutionStage() == EvolutionStageEnum.BASIC ? RarityEnum.DIAMOND : RarityEnum.DIAMOND_2;
                case FOIL -> RarityEnum.DIAMOND_3;
                case IMMERSIVE -> RarityEnum.STAR_3;
                case SHINY -> RarityEnum.SHINY;
                case GOLD -> RarityEnum.CROWN;
                case FULL_ART -> RarityEnum.STAR;
                default -> RarityEnum.DIAMOND;
            };
        } else {
            rarity = switch (card.effect()) {
                case RAINBOW, SPECIAL_ART -> RarityEnum.STAR_2;
                case IMMERSIVE -> RarityEnum.STAR_3;
                case SHINY -> RarityEnum.SHINY_2;
                case GOLD -> RarityEnum.CROWN;
                default -> RarityEnum.DIAMOND_4;
            };
        }
        return rarity;
    }

    private EffectEnum validateEffect(PokeCardVO card) {
        var effect = card.effect();
        if (card.category() == BattleCategoryEnum.NO_EX) {
            if (effect == EffectEnum.RAINBOW || effect == EffectEnum.SPECIAL_ART) {
                effect = EffectEnum.FULL_ART;
            } else if (effect == EffectEnum.EX) {
                effect = EffectEnum.FOIL;
            }
            return effect;
        }
        return switch (effect) {
            case COMMON, FOIL -> EffectEnum.EX;
            case FULL_ART -> EffectEnum.RAINBOW;
            default -> effect;
        };
    }
    public Mono<CardResponse> toResponse(Card card) {
        return switch (card) {
            case PokeCard poke -> toResponse(poke);
            case UtilCard util -> toResponse(util);
            default -> Mono.error(new IllegalArgumentException("Tipo de Card desconhecido: " + card.getClass()));
        };
    }

    public Mono<CardResponse> toResponse(PokeCard card) {
        return Mono.just(
                new CardResponse(
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
                )
        );
    }
    public Mono<CardResponse> toResponse(UtilCard card) {
        return Mono.just(
                new CardResponse(
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
                )
        );
    }

    private boolean validateEvolutionAndNoFossilPoke(PokeCardVO card, PokeSpeciesVO especie) {
        boolean validateNullable = especie.evolveFrom() != null;
        validateNullable = validateNullable && especie.evolveFrom().name() != null;
        boolean validateBasicPokemon = card.evolutionStage() != EvolutionStageEnum.BASIC;
        return validateNullable && validateBasicPokemon && !card.isFossil();
    }

    public Query getQuery(Map<String, String> filters) {
        return (filters.isEmpty()) ? null : new Query(new Criteria().andOperator(
                filters.entrySet().stream()
                        .map(e -> Criteria.where(e.getKey())
                                .regex(".*" + Pattern.quote(e.getValue()) + ".*", "i"))
                        .toArray(Criteria[]::new)
        ));
    }
}
