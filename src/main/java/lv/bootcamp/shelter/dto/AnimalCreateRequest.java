package lv.bootcamp.shelter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lv.bootcamp.shelter.model.AnimalType;

/**
 * JSON request body for creating a new animal via the REST API.
 * Status is not included; all new animals start as AVAILABLE.
 */
public record AnimalCreateRequest(

        @NotBlank(message = "Name is required")
        @Schema(
                description = "Animal name",
                example = "Milo"
        )
        String name,

        @NotNull(message = "Type is required")
        @Schema(
                description = "Animal type (species)",
                example = "CAT"
        )
        AnimalType type,

        @Schema(
                description = "Animal breed",
                example = "Siamese"
        )
        String breed,

        @Min(value = 0, message = "Age cannot be negative")
        @Schema(
                description = "Animal age",
                example = "5"
        )
        Integer age,

        @Schema(
                description = "Animal description",
                example = "Calm, loves when you rub its cheeks"
        )
        String description,

        @Schema(
                description = "Image url or local path",
                example = "/images/animals/Milo.jpg"
        )
        String imageUrl
) {}