package project.ee.dto.user;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.authentication.User;

@Component
public class UserToUserDTOConverter implements Converter<User,UserDTO> {
    @Override
    public UserDTO convert(User user) {
        if (user == null)
            return null;
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setBirthday(user.getBirthday());
        dto.setEmail(user.getEmail());
        dto.setImage(user.getImage());
        dto.setRole(user.getRole());
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
