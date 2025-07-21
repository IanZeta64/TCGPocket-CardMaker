package br.com.tcgpocket.cardmaker.service;

import br.com.tcgpocket.cardmaker.dataprovider.PokeCardDataProvider;
import br.com.tcgpocket.cardmaker.enums.*;
import br.com.tcgpocket.cardmaker.exceptions.CardCreationException;
import br.com.tcgpocket.cardmaker.exceptions.PokeNotFoundException;
import br.com.tcgpocket.cardmaker.model.PokeCard;
import br.com.tcgpocket.cardmaker.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
    private final PokeCardDataProvider pokeCardDataProvider;

    public CardService(PokeCardDataProvider pokeCardDataProvider) {
        this.pokeCardDataProvider = pokeCardDataProvider;
    }

    public Mono<PokeInfoVO> getPokeInfo(String user, PokeCardVO card) {
        return Mono.defer(() -> {
                            log.info("m=create, s=init, i=getPokeInfo, cardName={}, creator={}", card.name(), user);
                            return pokeCardDataProvider.getPokeDetail(card.name())
                                    .switchIfEmpty(Mono.error(new PokeNotFoundException("Details not found of poke: " + card.name())))
                                    .zipWith(pokeCardDataProvider.getPokeSpecies(card.name()))
                                    .switchIfEmpty(Mono.error(new PokeNotFoundException("Infos not found of poke: " + card.name())))
                                    .flatMap(tuple -> {
                                        var details = tuple.getT1();
                                        var species = tuple.getT2();

                                        var validateEvolutionAndNoFossilPoke = validateEvolutionAndNoFossilPoke(card, species);
                                        Mono<String> preEvolutionSprite = validateEvolutionAndNoFossilPoke
                                                ? pokeCardDataProvider.getPokeDetail(species.evolveFrom().name()).map(PokeDetailVO::getSprite)
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
                        card.isPromo() ? RarityEnum.PROMO : validateRarity(card),
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
        if (card.category() == BattleCategoryEnum.EX) {
            return switch (effect) {
                case COMMON, FOIL -> EffectEnum.EX;
                case FULL_ART -> EffectEnum.RAINBOW;
                default -> effect;
            };
        }
        if (effect == EffectEnum.RAINBOW || effect == EffectEnum.SPECIAL_ART) {
            return EffectEnum.FULL_ART;
        }
        if (effect == EffectEnum.EX) {
            return card.evolutionStage() == EvolutionStageEnum.BASIC ? EffectEnum.COMMON : EffectEnum.FOIL;
        }
        return effect;
    }

        public Mono<PokeCardResponse> toResponse (PokeCard entity){
            return Mono.just(new PokeCardResponse(
                    entity.getId(),
                    entity.getName(),
                    entity.getImage(),
                    entity.getBackground(),
                    entity.getEffect(),
                    entity.getCreatedBy(),
                    entity.getIllustrator(),
                    entity.getRarity(),
                    entity.getBooster(),
                    entity.getSpecie(),
                    entity.getCategory(),
                    entity.getType(),
                    entity.getEvolutionStage(),
                    entity.getDexNumber(),
                    entity.getDexInfo(),
                    entity.getPokeDescription(),
                    entity.getPs(),
                    entity.getAbility(),
                    entity.getAttack(),
                    entity.getWeakness(),
                    entity.getRetreat(),
                    entity.getEvolveFrom(),
                    entity.getEvolveFromSprite(),
                    entity.getStatus()

            ));
        }

        private boolean validateEvolutionAndNoFossilPoke (PokeCardVO card, PokeSpeciesVO especie){
            boolean validateNullable = especie.evolveFrom() != null;
            validateNullable = validateNullable && especie.evolveFrom().name() != null;
            boolean validateBasicPokemon = card.evolutionStage() != EvolutionStageEnum.BASIC;
            return validateNullable && validateBasicPokemon && !card.isFossil();
        }
    }
