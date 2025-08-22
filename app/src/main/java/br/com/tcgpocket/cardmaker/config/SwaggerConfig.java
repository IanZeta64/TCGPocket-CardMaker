package br.com.tcgpocket.cardmaker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("CardMaker API")
                                .version("1.0.0")
                                .description("This API is responsible for management cards of tcg-pocket.")
                ) .components(new io.swagger.v3.oas.models.Components()
                        .addSchemas("CardFilterMap",
                                new io.swagger.v3.oas.models.media.ObjectSchema()
                                        .addProperty("name", new io.swagger.v3.oas.models.media.StringSchema().example("Pikachu"))
                                        .addProperty("type", new io.swagger.v3.oas.models.media.StringSchema().example("ELECTRIC"))
                        )
                );
    }
}
