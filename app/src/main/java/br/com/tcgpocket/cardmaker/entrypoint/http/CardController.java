package br.com.tcgpocket.cardmaker.entrypoint.http;

import br.com.tcgpocket.cardmaker.vo.CardResponse;
import br.com.tcgpocket.cardmaker.vo.ImageChangeRequest;
import br.com.tcgpocket.cardmaker.vo.PokeCardRequest;
import br.com.tcgpocket.cardmaker.vo.UtilCardRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Tag(name = "Card Controller", description = "Manages the creation, update, search, and deletion of cards")
@RequestMapping("/card")
public interface CardController {

    @Operation(
            summary = "Create a Pokémon card",
            description = "Creates a new Pokémon card based on the provided data."
    )
    @ApiResponse(responseCode = "201", description = "Card successfully created",
            content = @Content(schema = @Schema(implementation = CardResponse.class)))
    @PostMapping("/create/poke")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CardResponse> createPokeCard(
            @Parameter(description = "Identifier of the user creating the card") @RequestHeader("X-user") String user,
            @RequestBody PokeCardRequest card);

    @Operation(
            summary = "Create a utility card",
            description = "Creates a new utility card."
    )
    @ApiResponse(responseCode = "201", description = "Card successfully created",
            content = @Content(schema = @Schema(implementation = CardResponse.class)))
    @PostMapping("/create/util")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CardResponse> createUtilCard(
            @Parameter(description = "Identifier of the user creating the card") @RequestHeader("X-user") String user,
            @RequestBody UtilCardRequest card);

    @Operation(
            summary = "Search cards",
            description = "Search for cards based on optional filters."
    )
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    Flux<CardResponse> searchCard(
            @Parameter(description = "Identifier of the user performing the search") @RequestHeader("X-user") String user,
            @Parameter(
                    name = "filters",
                    in = ParameterIn.QUERY,
                    description = "can send with no filters, just with '{ }' and it brings all cards",
                    schema = @Schema(
                            type = "object",
                            additionalProperties = Schema.AdditionalPropertiesValue.FALSE,
                            ref = "#/components/schemas/CardFilterMap"
                    ),
                    style = ParameterStyle.FORM,
                    explode = Explode.TRUE
            )
            @RequestParam Map<String, String> filters);

    @Operation(
            summary = "Get card by ID",
            description = "Retrieve details of a specific card by its ID."
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<CardResponse> getById(
            @Parameter(description = "Identifier of the requesting user") @RequestHeader("X-user") String user,
            @Parameter(description = "Card ID") @PathVariable String id);

    @Operation(
            summary = "Update a Pokémon card",
            description = "Updates the data of an existing Pokémon card."
    )
    @PutMapping("/update/poke/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<CardResponse> updatePokeCard(
            @Parameter(description = "Identifier of the user performing the update") @RequestHeader("X-user") String user,
            @Parameter(description = "Card ID") @PathVariable String id,
            @RequestBody PokeCardRequest card);

    @Operation(
            summary = "Update a utility card",
            description = "Updates the data of an existing utility card."
    )
    @PutMapping("/update/util/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<CardResponse> updateUtilCard(
            @Parameter(description = "Identifier of the user performing the update") @RequestHeader("X-user") String user,
            @Parameter(description = "Card ID") @PathVariable String id,
            @RequestBody UtilCardRequest card);

    @Operation(
            summary = "Change card image",
            description = "Replaces the image of a specific card."
    )
    @PatchMapping("/change-image/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<CardResponse> changeImage(
            @Parameter(description = "Identifier of the user changing the image") @RequestHeader("X-user") String user,
            @Parameter(description = "Card ID") @PathVariable String id,
            @RequestBody ImageChangeRequest request);

    @Operation(
            summary = "Promote or demote card",
            description = "Marks a card as promotional or removes its promotion status."
    )
    @PatchMapping("/promote/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<CardResponse> promote(
            @Parameter(description = "Identifier of the user performing the promotion") @RequestHeader("X-user") String user,
            @Parameter(description = "Card ID") @PathVariable String id,
            @Parameter(description = "True to promote, false to remove promotion") @RequestParam boolean promotion);

    @Operation(
            summary = "Delete a card",
            description = "Permanently deletes a card by its ID."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> delete(
            @Parameter(description = "Identifier of the user performing the deletion") @RequestHeader("X-user") String user,
            @Parameter(description = "Card ID") @PathVariable String id);
}
