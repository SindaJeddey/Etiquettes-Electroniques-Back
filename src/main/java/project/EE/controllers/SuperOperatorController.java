package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.EE.dto.UserDTO;
import project.EE.models.UserRoles;
import project.EE.services.UserService;

@RestController
@RequestMapping("/api/superops")
public class SuperOperatorController {

    private final UserService userService;

    public SuperOperatorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newSuperOperator(@RequestBody UserDTO dto){
        UserDTO savedDto = userService.saveNewUser(dto,UserRoles.SUPER_OPERATOR.name());
        return savedDto;
    }

}
