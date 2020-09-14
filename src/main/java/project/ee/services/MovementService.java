package project.ee.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.movement.MovementDTOToMovementConverter;
import project.ee.dto.movement.MovementToMovementDTOConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.InStoreProduct;
import project.ee.models.models.Movement;
import project.ee.models.models.MovementType;
import project.ee.models.models.Product;
import project.ee.repositories.InStoreProductRepository;
import project.ee.repositories.MovementRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final MovementToMovementDTOConverter toMovementDTOConverter;
    private final MovementDTOToMovementConverter toMovementConverter;
    private final InStoreProductRepository inStoreProductRepository;

    public MovementService(MovementRepository movementRepository,
                           MovementToMovementDTOConverter toMovementDTOConverter,
                           MovementDTOToMovementConverter toMovementConverter,
                           InStoreProductRepository inStoreProductRepository) {
        this.movementRepository = movementRepository;
        this.toMovementDTOConverter = toMovementDTOConverter;
        this.toMovementConverter = toMovementConverter;
        this.inStoreProductRepository = inStoreProductRepository;
    }

    public Set<MovementDTO> getMovements(String productId) throws NotFoundException {
        InStoreProduct inStoreProduct = inStoreProductRepository.findByInStoreProductCode(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: "+productId));
        return inStoreProduct.getMovements()
                .stream().map(movement -> toMovementDTOConverter.convert(movement))
                .collect(Collectors.toSet());
    }

    public MovementDTO getMovement(String  id) throws NotFoundException {
        return movementRepository.findByMovementCode(id)
                .map(toMovementDTOConverter::convert)
                .orElseThrow(() -> new NotFoundException("Movement code "+id+" not found"));
    }

    public void addMovement(MovementDTO movementDTO) throws NotFoundException {
        if(!movementDTO.getType().equals(MovementType.OUT.name())
                && !movementDTO.getType().equals(MovementType.IN.name())){
            throw new RuntimeException("Invalid transaction");
        }
        if(movementDTO.getProduct().getStore() == null || movementDTO.getProduct().getStore().getStoreCode() == null)
            throw new RuntimeException("Must provide the store");

        if(movementDTO.getProduct() == null || movementDTO.getProduct().getInStoreProductCode() == null) {
            throw new RuntimeException("Must provide a product");
        }

        if(movementDTO.getQuantity() == null ||movementDTO.getQuantity() < 0)
            throw new RuntimeException("Must provide a valid quantity");
        Movement movement = toMovementConverter.convert(movementDTO);
        movement.setMovementDate(LocalDate.now());
        movement.setMovementCode(RandomStringUtils.randomAlphabetic(10));
        InStoreProduct inStoreProduct = inStoreProductRepository.findByInStoreProductCode(movementDTO.getProduct().getInStoreProductCode())
                .orElseThrow(() -> new NotFoundException("Product in store id: "+movementDTO.getProduct().getInStoreProductCode()+" not found"));
        movement.setProduct(inStoreProduct);
        if(movement.getType().equals(MovementType.OUT.name()))
            inStoreProduct.setQuantity(inStoreProduct.getQuantity()-movement.getQuantity());
        else
            inStoreProduct.setQuantity(inStoreProduct.getQuantity()+movement.getQuantity());

        if(inStoreProduct.getProduct().getQuantityThreshold() > inStoreProduct.getQuantity())
            inStoreProduct.setAlertThreshold(true   );

        inStoreProduct.addMovement(movement);
        inStoreProductRepository.save(inStoreProduct);
    }
}
