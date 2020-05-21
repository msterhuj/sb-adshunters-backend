package space.netbytes.adshunters.repositories.domains;

import space.netbytes.adshunters.models.domains.Domain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DomainRepository extends CrudRepository<Domain, UUID> {
}
