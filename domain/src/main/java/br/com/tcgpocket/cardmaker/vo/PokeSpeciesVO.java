package br.com.tcgpocket.cardmaker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokeSpeciesVO(
        @JsonProperty("evolves_from_species")
        EvolveFromVO evolveFrom,
        @JsonProperty("flavor_text_entries")
        List<FlavorTextVO> dexDescription,
        @JsonProperty("genera")
        List<DexInfoVO> dexInfo
) {

        public String getDexInfo(){
                return dexInfo.get(7).getText();
        }

        public String getDexDescription(){
                return dexDescription.getFirst().getText();
        }
}
