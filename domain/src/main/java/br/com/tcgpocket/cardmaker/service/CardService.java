package br.com.tcgpocket.cardmaker.service;

import br.com.tcgpocket.cardmaker.dataprovider.CardDataProvider;
import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.exceptions.PokeNotFoundException;
import br.com.tcgpocket.cardmaker.exceptions.NotUpdateCardException;
import br.com.tcgpocket.cardmaker.model.Card;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.model.UtilCard;
import br.com.tcgpocket.cardmaker.model.Visual;
import br.com.tcgpocket.cardmaker.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
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

    public Mono<PokeInfoVO> getPokeInfoFromExternalSource(String user, PokeCardRequest request) {
        return Mono.defer(() -> {
                            log.info("m=create, s=init, i=getPokeInfo, cardName={}, creator={}", request.name(), user);
                            return cardDataProvider.getPokeDetail(request.name())
                                    .switchIfEmpty(Mono.error(new PokeNotFoundException("Details not found of poke: " + request.name())))

                                    .zipWith(cardDataProvider.getPokeSpecies(request.name()))
                                    .switchIfEmpty(Mono.error(new PokeNotFoundException("Infos not found of poke: " + request.name())))

                                    .flatMap(tuple -> buildPokeInfoFromExternalSource(request, tuple.getT1(), tuple.getT2()));
                        }
                )
                .doOnNext(it -> log.info("m=getPokeInfo, s=finished, i=getPokeInfo, cardName={}, creator={}", request.name(), user))
                .doOnError(ex -> log.error("m=getPokeInfo, s=error, i=getPokeInfo, ex={}, message={}, cardName={}, creator={}", ex.getClass(), ex.getMessage(), request.name(), user))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<PokeInfoVO> buildPokeInfoFromExternalSource(PokeCardRequest request, PokeDetailVO details, PokeSpeciesVO species) {
        var validateEvolutionAndNoFossilPoke = validateEvolutionAndNoFossilPoke(request, species);
        Mono<String> preEvolutionSprite = validateEvolutionAndNoFossilPoke
                ? cardDataProvider.getPokeDetail(species.evolveFrom().name()).map(PokeDetailVO::getSprite)
                .switchIfEmpty(Mono.error(new PokeNotFoundException("Details not found of poke: " + request.name())))
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
    }

    public Mono<PokeInfoVO> getPokeInfoFromCache(String name) {
        return Mono.defer(() -> {
                            log.info("m=getPokeInfoFromCache, s=init, i=getPokeInfo, cardName={}", name);
                            return cardDataProvider.getPokeInfoFromCache(name);
                        }
                ).doOnNext(it -> log.info("m=getPokeInfoFromCache, s=finished, i=getPokeInfo, cardName={}, pokeInfo={}", name, it))
                .doOnError(ex -> log.error("m=getPokeInfoFromCache, s=warn, i=getPokeInfo, ex={}, message={}, cardName={}", ex.getClass(), ex.getMessage(), name))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<PokeInfoVO> savePokeInfoInCache(String name, PokeInfoVO pokeInfo) {
        return Mono.defer(() -> {
                            log.info("m=savePokeInfoInCache, s=init, i=savePokeInfo, cardName={}, pokeInfo={}", name, pokeInfo);
                            return cardDataProvider.savePokeInfoInCache(name, pokeInfo);
                        }
                ).doOnNext(it -> log.info("m=savePokeInfoInCache, s=finished, i=savePokeInfo, cardName={}, pokeInfo={}", name, pokeInfo))
                .doOnError(ex -> log.error("m=savePokeInfoInCache, s=error, i=savePokeInfo, ex={}, message={}, cardName={}, pokeInfo={}", ex.getClass(), ex.getMessage(), name, pokeInfo))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<PokeCard> buildModelWithOfficialPoke(String user, PokeCardRequest request, PokeInfoVO pokeInfo) {
        boolean validateImage = request.image() != null;
        return Mono.just(new PokeCard(
                        request.battleCategory() != BattleCategoryEnum.EX ? request.name() : request.name() + "-EX",
                        validateImage ? request.image() : validateShiny(request.isShiny(), pokeInfo),
                        request.imageLine(),
                        request.backgroundImage(),
                        request.backgroundEffect(),
                        request.ex3dEffect(),
                        validateEffect(request),
                        user,
                        validateImage ? request.illustrator() : null,
                        Boolean.TRUE.equals(request.isPromo()) ? RarityEnum.PROMO : validateRarity(request),
                        request.booster(),
                        PromoteStatusEnum.PRIVATE,
                        request.name(),
                        request.battleCategory(),
                        request.type(),
                        request.evolutionStage(),
                        pokeInfo.dexNumber(),
                        String.format("Nº%04d %s Height: %s ft Weight: %s lb.", pokeInfo.dexNumber(), pokeInfo.dexInfo(), pokeInfo.height(), pokeInfo.height()),
                        pokeInfo.dexDescription().replace("\\n", " ").replace("\f", " "),
                        request.ps(),
                        request.ability(),
                        request.attack(),
                        request.weakness(),
                        request.retreat(),
                        pokeInfo.evolveFrom(),
                        pokeInfo.evolveFromSprite()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private String validateShiny(boolean isShiny, PokeInfoVO pokeInfo) {
        return isShiny ? pokeInfo.shinyImage() : pokeInfo.defaultImage();
    }

    public Mono<PokeCard> buildModel(String user, PokeCardRequest request) {
        return Mono.just(new PokeCard(
                        request.battleCategory() != BattleCategoryEnum.EX ? request.name() : request.name() + "-EX",
                        request.image(),
                        request.imageLine(),
                        request.backgroundImage(),
                        request.backgroundEffect(),
                        request.ex3dEffect(),
                        validateEffect(request),
                        user,
                        request.illustrator(),
                        Boolean.TRUE.equals(request.isPromo()) ? RarityEnum.PROMO : validateRarity(request),
                        request.booster(),
                        PromoteStatusEnum.PRIVATE,
                        request.name(),
                        request.battleCategory(),
                        request.type(),
                        request.evolutionStage(),
                        request.dexNumber(),
                        String.format("Nº %s %s.", request.dexNumber(), request.dexInfo()),
                        request.pokeDescription(),
                        request.ps(),
                        request.ability(),
                        request.attack(),
                        request.weakness(),
                        request.retreat(),
                        request.evolveFrom(),
                        request.evolveFromSprite()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<UtilCard> buildModel(String user, UtilCardRequest request) {
        return Mono.just(
                new UtilCard(
                        request.name(),
                        request.image(),
                        request.imageLine(),
                        request.backgroundImage(),
                        request.backgroundEffect(),
                        request.ex3dEffect(),
                        validateEffect(request),
                        user,
                        request.illustrator(),
                        validateRarity(request),
                        request.booster(),
                        PromoteStatusEnum.PRIVATE,
                        request.utilType(),
                        request.description()
                )
        );
    }

    public Mono<PokeCard> updateModel(String user, PokeCardRequest request, PokeCard model) {
        return Mono.just(new PokeCard(
                        model.getId(),
                        request.battleCategory() != BattleCategoryEnum.EX ? request.name() : request.name() + "-EX",
                        request.image(),
                        request.imageLine(),
                        request.backgroundImage(),
                        request.backgroundEffect(),
                        request.ex3dEffect(),
                        validateEffect(request),
                        user,
                        request.illustrator(),
                        Boolean.TRUE.equals(request.isPromo()) ? RarityEnum.PROMO : validateRarity(request),
                        request.booster(),
                        model.getStatus(),
                        model.getCreatedAt(),
                        request.name(),
                        request.battleCategory(),
                        request.type(),
                        request.evolutionStage(),
                        request.dexNumber(),
                        String.format("Nº %s %s.", request.dexNumber(), request.dexInfo()),
                        request.pokeDescription(),
                        request.ps(),
                        request.ability(),
                        request.attack(),
                        request.weakness(),
                        request.retreat(),
                        request.evolveFrom(),
                        request.evolveFromSprite()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<UtilCard> updateModel(String user, UtilCardRequest request, UtilCard model) {
        return Mono.just(new UtilCard(
                        model.getId(),
                        request.name(),
                        request.image(),
                        request.imageLine(),
                        request.backgroundImage(),
                        request.backgroundEffect(),
                        request.ex3dEffect(),
                        validateEffect(request),
                        user,
                        request.illustrator(),
                        Boolean.TRUE.equals(request.isPromo()) ? RarityEnum.PROMO : validateRarity(request),
                        request.booster(),
                        model.getStatus(),
                        model.getCreatedAt(),
                        request.utilType(),
                        request.description()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Card changeImage(Card card, ImageChangeRequest newVisual) {
        if (card.getStatus() != PromoteStatusEnum.PRIVATE)
            throw new NotUpdateCardException("Cannot change image of a promoted: " + card.getId());
        if (newVisual.image() != null)
            card.setVisual(new Visual(newVisual.image(), newVisual.imageLine(), newVisual.backgroundImage(), newVisual.backgroundEffect(), newVisual.ex3dEffect(), card.getVisual().getCategoryEffect()));
        return card;
    }

    public Card promote(Card card, boolean promotion) {
        var status = card.getStatus();
        if (status.isFinal()) throw new NotUpdateCardException("Card is in final status: " + card.getId());
        card.setStatus(status.promote(promotion));
        return card;
    }

    private RarityEnum validateRarity(UtilCardRequest request) {
        return Boolean.TRUE.equals((request.isPromo())) ? RarityEnum.PROMO : switch (request.categoryEffect()) {
            case SPECIAL_ART, FULL_ART, RAINBOW -> RarityEnum.STAR_2;
            case IMMERSIVE -> RarityEnum.STAR_3;
            case GOLD -> RarityEnum.CROWN;
            default -> RarityEnum.DIAMOND_2;
        };
    }

    private RarityEnum validateRarity(PokeCardRequest request) {
        if (Boolean.TRUE.equals(request.isPromo())) return RarityEnum.PROMO;
        RarityEnum rarity;
        if (request.battleCategory() == BattleCategoryEnum.NO_EX) {
            rarity = switch (request.categoryEffect()) {
                case COMMON ->
                        request.evolutionStage() == EvolutionStageEnum.BASIC ? RarityEnum.DIAMOND : RarityEnum.DIAMOND_2;
                case FOIL -> RarityEnum.DIAMOND_3;
                case IMMERSIVE -> RarityEnum.STAR_3;
                case SHINY -> RarityEnum.SHINY;
                case GOLD -> RarityEnum.CROWN;
                case FULL_ART -> RarityEnum.STAR;
                default -> RarityEnum.DIAMOND;
            };
        } else {
            rarity = switch (request.categoryEffect()) {
                case RAINBOW, SPECIAL_ART -> RarityEnum.STAR_2;
                case IMMERSIVE -> RarityEnum.STAR_3;
                case SHINY -> RarityEnum.SHINY_2;
                case GOLD -> RarityEnum.CROWN;
                default -> RarityEnum.DIAMOND_4;
            };
        }
        return rarity;
    }

    private CategoryEffectEnum validateEffect(UtilCardRequest card) {
        return switch (card.categoryEffect()) {
            case COMMON, FOIL, EX -> CategoryEffectEnum.COMMON;
            case FULL_ART, SPECIAL_ART, RAINBOW, SHINY -> CategoryEffectEnum.SPECIAL_ART;
            case IMMERSIVE -> CategoryEffectEnum.IMMERSIVE;
            case GOLD -> CategoryEffectEnum.GOLD;
        };
    }

    private CategoryEffectEnum validateEffect(PokeCardRequest card) {
        var effect = card.categoryEffect();
        if (card.battleCategory() == BattleCategoryEnum.NO_EX) {
            if (effect == CategoryEffectEnum.RAINBOW || effect == CategoryEffectEnum.SPECIAL_ART) {
                effect = CategoryEffectEnum.FULL_ART;
            } else if (effect == CategoryEffectEnum.EX) {
                effect = CategoryEffectEnum.FOIL;
            }
            return effect;
        }
        return switch (effect) {
            case COMMON, FOIL -> CategoryEffectEnum.EX;
            case FULL_ART -> CategoryEffectEnum.RAINBOW;
            default -> effect;
        };
    }

    private boolean validateEvolutionAndNoFossilPoke(PokeCardRequest card, PokeSpeciesVO especie) {
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
