package lv.bootcamp.shelter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;

/**
 * Response body for a single animal returned by the API.
 * {@code adoptionNote} is only populated for ADMIN callers (see AnimalService#toResponse) —
 * everyone else just sees the plain {@code status}.
 */
public record AnimalResponse(
        @Schema(
                description = "Unique animal ID",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Animal name",
                example = "Milo"
        )
        String name,
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
        @Schema(
                description = "Animal age in years",
                example = "5"
        )
        Integer age,
        @Schema(
                description = "Animal description",
                example = "Calm, loves when you rub its cheeks"
        )
        String description,
        @Schema(
                description = "Current adoption status",
                example = "AVAILABLE"
        )
        AnimalStatus status,
        @Schema(
                description = "Image url or local path",
                example = "/images/animals/Milo.jpg"
        )
        String imageUrl,
        @Schema(
                description = "Adoption information. Visible only for admins",
                example = "adopted by user on 2026-06-01"
        )
        String adoptionNote
) {}
