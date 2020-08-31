package project.ee.dto.movement;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import project.ee.dto.inStoreProduct.InStoreProductDTOToInStoreProductConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.InStoreProduct;
import project.ee.models.models.Movement;

@Component
public class MovementDTOToMovementConverter implements Converter<MovementDTO, Movement> {

    private final InStoreProductDTOToInStoreProductConverter toInStoreProductConverter;

    public MovementDTOToMovementConverter(InStoreProductDTOToInStoreProductConverter toInStoreProductConverter) {
        this.toInStoreProductConverter = toInStoreProductConverter;
    }

    @SneakyThrows
    @Override
    public Movement convert(MovementDTO movementDTO) {
        if (movementDTO == null)
            return null;
        Movement movement = new Movement();
        movement.setId(movementDTO.getId());
        movement.setType(movementDTO.getType());
        movement.setQuantity(movementDTO.getQuantity());
        movement.setMovementDate(movementDTO.getMovementDate());
        movement.setProduct(toInStoreProductConverter.convert(movementDTO.getProduct()));
        return movement;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
