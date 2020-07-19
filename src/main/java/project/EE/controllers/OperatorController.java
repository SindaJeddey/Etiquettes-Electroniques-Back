package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.UserDTO;
import project.EE.models.UserRoles;
import project.EE.services.UserService;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    private final UserService userService;

    public OperatorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newOperator(@RequestBody UserDTO dto){
        UserDTO savedDto = userService.saveNewUser(dto,UserRoles.OPERATOR.name());
        return savedDto;
    }
}