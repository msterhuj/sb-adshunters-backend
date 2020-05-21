package space.netbytes.adshunters.services.domains;

import space.netbytes.adshunters.converters.Converter;
import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.domains.Domain;
import space.netbytes.adshunters.models.domains.Vote;
import space.netbytes.adshunters.repositories.domains.DomainRepository;
import space.netbytes.adshunters.repositories.domains.VoteRepository;
import space.netbytes.adshunters.services.KeycloakService;
import space.netbytes.adshunters.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VoteService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private KeycloakService keycloakService;

    public List<Vote> getAllVote() {
        return (List<Vote>) voteRepository.findAll();
    }

    public Vote getVote(UUID voteId) throws ResourceNotFoundException {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote not found for this uuid :: " + voteId));
    }

    public Vote createVote(UUID domainId, Vote vote) throws ResourceNotFoundException { //todo check if vote exist
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + domainId));
        vote.setCreatedAt(new Date());
        vote.setDomain(domain);
        vote.setUser(userDetailsService.getUserDetails(keycloakService.getUUID()));
        return voteRepository.save(vote);
    }

    public Vote updateVote(Vote voteNew) throws ResourceNotFoundException {
        Vote vote = voteRepository.findById(voteNew.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vote not found for this uuid :: " + voteNew.getId()));
        Converter.copyNotNullProperties(voteNew, vote);
        return voteRepository.save(vote);
    }

    public void deleteVote(UUID uuid) throws ResourceNotFoundException {
        Vote vote = voteRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Domain vote not found for this uuid : " + uuid));
        voteRepository.delete(vote);
    }
}
