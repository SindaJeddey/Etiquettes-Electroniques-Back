package project.ee.models.authentication;

import lombok.*;

import javax.persistence.*;
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
    private String username;

    private String password;

    private String name;

    private String lastName;

    private LocalDate birthday;

    @Column(unique = true)
    private String email;

    private String role;

    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reset_token_id")
    private PasswordResetToken resetToken;
}
