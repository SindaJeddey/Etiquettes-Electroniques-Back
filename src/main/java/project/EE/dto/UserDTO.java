package project.EE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String lastName;
    private LocalDate birthday;
    private String email;
    private String role;
}
