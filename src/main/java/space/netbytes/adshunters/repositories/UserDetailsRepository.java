package space.netbytes.adshunters.repositories;

import space.netbytes.adshunters.models.UserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, UUID> {
}
