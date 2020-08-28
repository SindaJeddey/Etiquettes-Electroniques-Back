package project.ee.services;

import org.springframework.stereotype.Service;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.movement.MovementToMovementDTOConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Movement;
import project.ee.repositories.MovementRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final MovementToMovementDTOConverter toMovementDTOConverter;

    public MovementService(MovementRepository movementRepository,
                           MovementToMovementDTOConverter toMovementDTOConverter) {
        this.movementRepository = movementRepository;
        this.toMovementDTOConverter = toMovementDTOConverter;
    }

    public Set<MovementDTO> getMovements(){
        return movementRepository.findAll()
                .stream()
                .map(toMovementDTOConverter::convert)
                .collect(Collectors.toSet());
    }

    public MovementDTO getMovement(Long id) throws NotFoundException {
        return movementRepository.findById(id)
                .map(toMovementDTOConverter::convert)
                .orElseThrow(() -> new NotFoundException("Movement id "+id+" not found"));
    }

    public void deleteMovement(Long id) throws NotFoundException {
        Movement toDelete = movementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movement id "+id+" not found"));
        movementRepository.delete(toDelete);
    }
}
