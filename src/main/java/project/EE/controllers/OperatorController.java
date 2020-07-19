package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.UserDTO;
import project.EE.dto.UserDTOToUserConverter;
import project.EE.dto.UserToUserDTOConverter;
import project.EE.models.User;
import project.EE.models.UserRoles;
import project.EE.services.UserService;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    private final UserService userService;
    private final UserDTOToUserConverter toUserConverter;
    private final UserToUserDTOConverter toUserDTOConverter;

    public OperatorController(UserService userService,
                              UserDTOToUserConverter toUserConverter,
                              UserToUserDTOConverter toUserDTOConverter) {
        this.userService = userService;
        this.toUserConverter = toUserConverter;
        this.toUserDTOConverter = toUserDTOConverter;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newOperator(@RequestBody UserDTO dto){
        dto.setRole(UserRoles.OPERATOR.name());
        User toSave = toUserConverter.convert(dto);
        User savedUser = userService.saveNewUser(toSave);
        UserDTO savedDto = toUserDTOConverter.convert(savedUser);
        savedDto.setPassword(null);
        return savedDto;
    }
}