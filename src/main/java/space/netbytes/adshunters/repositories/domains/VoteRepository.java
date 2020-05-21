package space.netbytes.adshunters.repositories.domains;

import space.netbytes.adshunters.models.domains.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoteRepository extends CrudRepository<Vote, UUID> {
}
