package project.ee.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.movement.MovementDTOToMovementConverter;
import project.ee.dto.movement.MovementToMovementDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.exceptions.ResourceNotValidException;
import project.ee.models.models.InStoreProduct;
import project.ee.models.models.Movement;
import project.ee.models.models.MovementType;
import project.ee.repositories.InStoreProductRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private final MovementToMovementDTOConverter toMovementDTOConverter;
    private final MovementDTOToMovementConverter toMovementConverter;
    private final InStoreProductRepository inStoreProductRepository;

    public MovementService(MovementToMovementDTOConverter toMovementDTOConverter,
                           MovementDTOToMovementConverter toMovementConverter,
                           InStoreProductRepository inStoreProductRepository) {
        this.toMovementDTOConverter = toMovementDTOConverter;
        this.toMovementConverter = toMovementConverter;
        this.inStoreProductRepository = inStoreProductRepository;
    }

    public Set<MovementDTO> getMovements(String productId) {
        InStoreProduct inStoreProduct = inStoreProductRepository.findByInStoreProductCode(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product in store not found: "+productId));
        return inStoreProduct.getMovements()
                .stream()
                .map(toMovementDTOConverter::convert)
                .collect(Collectors.toSet());
    }

    public void addMovement(MovementDTO movementDTO){
        if(!movementDTO.getType().equals(MovementType.OUT.name())
                && !movementDTO.getType().equals(MovementType.IN.name())){
            throw new ResourceNotValidException("Invalid transaction");
        }
        if(movementDTO.getProduct().getStore() == null ||
                movementDTO.getProduct().getStore().getStoreCode() == null ||
                movementDTO.getProduct().getStore().getStoreCode().isEmpty() )
            throw new ResourceNotValidException("Must provide the store");

        if(movementDTO.getProduct() == null ||
                movementDTO.getProduct().getInStoreProductCode() == null ||
                movementDTO.getProduct().getInStoreProductCode().isEmpty())  {
            throw new ResourceNotValidException("Must provide a product");
        }

        if(movementDTO.getQuantity() == null ||movementDTO.getQuantity() < 0)
            throw new ResourceNotValidException("Must provide a valid quantity");

        Movement movement = toMovementConverter.convert(movementDTO);
        movement.setMovementDate(LocalDate.now());
        movement.setMovementCode(RandomStringUtils.randomAlphabetic(10));
        InStoreProduct inStoreProduct = inStoreProductRepository.findByInStoreProductCode(movementDTO.getProduct().getInStoreProductCode())
                .filter(inStoreProduct1 -> inStoreProduct1.getStore().getStoreCode().equals(movementDTO.getProduct().getStore().getStoreCode()))
                .orElseThrow(() -> new ResourceNotFoundException("Product in store id: "+movementDTO.getProduct().getInStoreProductCode()+" not found"));
        if(movement.getType().equals(MovementType.OUT.name()))
            inStoreProduct.setQuantity(inStoreProduct.getQuantity()-movement.getQuantity());
        else
            inStoreProduct.setQuantity(inStoreProduct.getQuantity()+movement.getQuantity());
        if(inStoreProduct.getProduct().getQuantityThreshold() > inStoreProduct.getQuantity())
            inStoreProduct.setAlertThreshold(true);
        inStoreProduct.addMovement(movement);
        inStoreProductRepository.save(inStoreProduct);
    }
}
