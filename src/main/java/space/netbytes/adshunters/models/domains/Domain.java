package space.netbytes.adshunters.models.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import space.netbytes.adshunters.models.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Domain {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("domains")
    private UserDetails user;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @JsonIgnoreProperties("domains")
    @ManyToMany
    @JoinTable(name = "domain_category",
            joinColumns = @JoinColumn(name = "domain_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories;

    @Column(nullable = false)
    private Date createdAt;

    @JsonIgnoreProperties("domain")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "domain")
    private List<Vote> votes;

    // comments (one to one)
}
