package lv.bootcamp.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for shelter animal endpoints.
 * Returns JSON — does not render HTML pages.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
@Tag(
        name = "Animals",
        description = "Animal management endpoints"
)
public class AnimalApiController {

    private final AnimalService animalService;

    @GetMapping
    @Operation(
            summary = "Get all animals",
            description = "Returns all animals in the shelter"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Animals returned successfully"
    )
    public List<AnimalResponse> findAll() {
        return animalService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find animal by id",
            description = "Returns animal with corresponding id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Animal found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Animal not found"
    )
    public ResponseEntity<AnimalResponse> findById(@PathVariable Long id) {
        return animalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists adopted animals. Restricted to ROLE_ADMIN — see SecurityConfig.
     * Read-only, so it's a good endpoint for testing role-based JWT authorization:
     * calling it repeatedly (e.g. with/without a token, or with a ROLE_USER token)
     * has no side effects, unlike {@code POST /api/animals}.
     */
    @GetMapping("/adopted")
    @Operation(
            summary = "Get adopted animals",
            description = "Returns all adopted animals. Requires ADMIN role."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Adopted animals returned successfully"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Access denied"
    )
    public List<AnimalResponse> findAdopted() {
        return animalService.findAdopted();
    }

    /**
     * Creates a new animal. Restricted to ROLE_ADMIN — see SecurityConfig.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new animal",
            description = "Creates a new animal. Requires ADMIN role."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Animal created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid animal data"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Access denied"
    )
    public AnimalResponse create(@RequestBody @Valid AnimalCreateRequest request) {
        return animalService.create(request);
    }

    /**
     * Adopts an animal as the currently logged-in user. Restricted to ROLE_USER
     * (not ROLE_ADMIN) — see SecurityConfig.
     */
    @PostMapping("/{id}/adopt")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Adopt an animal",
            description = "Marks an animal as adopted by the logged-in user. Requires USER role."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Animal adopted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Animal not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Animal is already adopted"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Access denied"
    )
    public ResponseEntity<AnimalResponse> adopt(@PathVariable Long id, Authentication authentication) {
        return animalService.adopt(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleAlreadyAdopted(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
