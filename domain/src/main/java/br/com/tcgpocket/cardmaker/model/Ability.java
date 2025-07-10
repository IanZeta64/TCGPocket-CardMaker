package br.com.tcgpocket.cardmaker.model;

import br.com.tcgpocket.cardmaker.enums.PokeTypeEnum;

import java.util.List;

public class Ability {
    private List<PokeTypeEnum> energy;
    private Integer damage;
    private String description;

    public Ability() {
    }

    public Ability(List<PokeTypeEnum> energy, Integer damage, String description) {
        this.energy = energy;
        this.damage = damage;
        this.description = description;
    }


    public List<PokeTypeEnum> getEnergy() {
        return energy;
    }

    public void setEnergy(List<PokeTypeEnum> energy) {
        this.energy = energy;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
