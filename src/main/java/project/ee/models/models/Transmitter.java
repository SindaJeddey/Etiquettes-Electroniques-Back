package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transmitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Must provide a transmitter name")
    private String name;

    @NotEmpty(message = "Must provide a transmitter code")
    private String code;

    @OneToMany(mappedBy = "transmitter")
    private Set<Tag> tags = new HashSet<>();

    public void addTag (Tag tag){
        if(this.tags == null)
            this.tags = new HashSet<>();
        if(this.tags.contains(tag))
            this.tags.remove(tag);
        this.tags.add(tag);
        tag.setTransmitter(this);
    }

    public void removeTag(Tag tag){
        this.tags.remove(tag);
        tag.setTransmitter(null);
    }
}
