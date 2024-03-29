package project.ee.dto.movement;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Movement;

@Component
public class MovementToMovementDTOConverter implements Converter<Movement,MovementDTO> {

    @Override
    public MovementDTO convert(Movement movement) {
        if(movement == null)
            return null;
        MovementDTO dto = new MovementDTO();
        dto.setMovementCode(movement.getMovementCode());
        dto.setType(movement.getType());
        dto.setQuantity(movement.getQuantity());
        dto.setMovementDate(movement.getMovementDate());
        return dto;
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
