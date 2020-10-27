package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.movement.MovementDTO;
import project.ee.services.MovementService;

import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN','ROLE_SUPER_OPERATOR')")
@Validated
public class MovementController {
    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PutMapping("/add")
    public void addTransaction (@RequestBody MovementDTO movementDTO){
        movementService.addMovement(movementDTO);
    }

    @GetMapping("/{productId}")
    public Set<MovementDTO> getTransactions(@PathVariable String productId){
        return movementService.getMovements(productId);
    }
}
