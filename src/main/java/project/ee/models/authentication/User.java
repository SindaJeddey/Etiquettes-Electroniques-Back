package project.ee.models.authentication;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @NotEmpty(message = "Must provide username")
    private String username;

    @NotEmpty(message = "Must provide password")
    private String password;

    private String name;

    private String lastName;

    @Past
    private LocalDate birthday;

    @Column(unique = true)
    @Email
    @NotEmpty(message = "Must provide email")
    private String email;

    @NotEmpty(message = "Must provide role")
    private String role;

    private byte[] image;

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
            mappedBy = "user")
    private Set<PasswordResetToken> resetTokens;

    public void addResetToken (PasswordResetToken passwordResetToken){
        if (resetTokens == null)
            this.setResetTokens(new HashSet<>());
        if (this.resetTokens.contains(passwordResetToken))
            this.resetTokens.remove(passwordResetToken);
        this.resetTokens.add(passwordResetToken);
        passwordResetToken.setUser(this);
    }
}
