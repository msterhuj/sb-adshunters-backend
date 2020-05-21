package space.netbytes.adshunters.models.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String description;

    @JsonIgnoreProperties("categories")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<Domain> domains;

    @Column(nullable = false)
    private Date createdAt;
}
