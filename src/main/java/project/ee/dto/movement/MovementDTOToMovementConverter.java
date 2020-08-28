package project.ee.dto.movement;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.models.models.Movement;

@Component
public class MovementDTOToMovementConverter implements Converter<MovementDTO, Movement> {

    private final ProductDTOToProductConverter toProductConverter;

    public MovementDTOToMovementConverter(ProductDTOToProductConverter toProductConverter) {
        this.toProductConverter = toProductConverter;
    }

    @Override
    public Movement convert(MovementDTO movementDTO) {
        if (movementDTO == null)
            return null;
        Movement movement = new Movement();
        movement.setId(movementDTO.getId());
        movement.setType(movementDTO.getType());
        movement.setQuantity(movementDTO.getQuantity());
        movement.setMovementDate(movementDTO.getMovementDate());
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
