package br.com.tcgpocket.cardmaker.vo;

import br.com.tcgpocket.cardmaker.enums.BackgroundEnum;

public record ImageChangeRequest(
        String image,
        BackgroundEnum background
) {
}
