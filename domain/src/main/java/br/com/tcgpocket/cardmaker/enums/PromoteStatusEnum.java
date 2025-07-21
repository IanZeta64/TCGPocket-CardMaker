package br.com.tcgpocket.cardmaker.enums;

public enum PromoteStatusEnum {
    PRIVATE,
    ON_VOTE,
    PROMOTED,
    COMPETITIVE,
    REFUSED;

    public PromoteStatusEnum promote(boolean approved) {
        if (!approved) return REFUSED;

        return switch (this) {
            case PRIVATE -> ON_VOTE;
            case ON_VOTE -> PROMOTED;
            case PROMOTED -> COMPETITIVE;
            case COMPETITIVE, REFUSED -> this;
        };
    }

    public boolean isFinal() {
        return this == COMPETITIVE || this == REFUSED;
    }
}
