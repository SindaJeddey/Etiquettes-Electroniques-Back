package project.ee.models.authentication;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String name;

    private String lastName;

    @Past
    private LocalDate birthday;

    @Column(unique = true)
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String role;

    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reset_token_id")
    private PasswordResetToken resetToken;
}
