package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.movement.MovementDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.services.MovementService;

import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
@PreAuthorize("permitAll()")
//@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN')")
public class MovementController {
    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PutMapping("/add")
    public void addTransaction (@RequestBody MovementDTO movementDTO) throws NotFoundException {
        movementService.addMovement(movementDTO);
    }

    @GetMapping("/{productId}")
    public Set<MovementDTO> getTransactions(@PathVariable String productId) throws NotFoundException {
        return movementService.getMovements(productId);
    }
}
