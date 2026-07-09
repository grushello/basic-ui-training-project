package lv.bootcamp.shelter.controller;

import jakarta.validation.Valid;
import lv.bootcamp.shelter.form.AnimalForm;
import lv.bootcamp.shelter.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AnimalPageController {

    private final AnimalService animalService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {

        addRoles(model, authentication);

        return "index";
    }


    @GetMapping("/animals")
    public String animals(Model model, Authentication authentication) {

        model.addAttribute("animals", animalService.findAll());

        addRoles(model, authentication);

        return "animals";
    }


    @GetMapping("/animals/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newAnimal(Model model, Authentication authentication) {

        model.addAttribute(
                "form",
                new AnimalForm(null, null, null, null, null, null)
        );

        addRoles(model, authentication);

        return "animals-new";
    }


    @PostMapping("/animals")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAnimal(
            @Valid @ModelAttribute("form") AnimalForm form,
            BindingResult bindingResult,
            Model model,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            addRoles(model, authentication);
            return "animals-new";
        }

        animalService.createFromForm(form);

        return "redirect:/animals";
    }


    private void addRoles(Model model, Authentication authentication) {

        boolean isAdmin = false;
        boolean isUser = false;

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {

            isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            isUser = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        }


        boolean isAuthenticated =
                authentication != null &&
                        authentication.isAuthenticated() &&
                        !authentication.getPrincipal().equals("anonymousUser");


        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
        model.addAttribute("isAuthenticated", isAuthenticated);
    }

}