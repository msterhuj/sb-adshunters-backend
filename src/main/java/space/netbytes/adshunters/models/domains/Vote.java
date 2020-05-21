package space.netbytes.adshunters.models.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import space.netbytes.adshunters.models.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("votes")
    private UserDetails user;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    @JsonIgnoreProperties("votes")
    private Domain domain;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private Date createdAt;
}
