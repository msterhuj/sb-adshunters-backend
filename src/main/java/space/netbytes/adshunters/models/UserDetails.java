package space.netbytes.adshunters.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import space.netbytes.adshunters.models.domains.Domain;
import space.netbytes.adshunters.models.domains.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue
    private UUID id;

    @JsonIgnoreProperties("user")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Domain> domains;

    @JsonIgnoreProperties("user")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Vote> votes;
}
