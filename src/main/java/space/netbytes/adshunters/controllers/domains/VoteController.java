package space.netbytes.adshunters.controllers.domains;

import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.domains.Vote;
import space.netbytes.adshunters.services.domains.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping
    public List<Vote> getAllVote() {
        return voteService.getAllVote();
    }

    @GetMapping("/{voteId}")
    public Vote getVote(@PathVariable(value = "voteId") UUID voteId) throws ResourceNotFoundException {
        return voteService.getVote(voteId);
    }

    @PostMapping("/{domainId}")
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public Vote createVote(@PathVariable(value = "domainId") UUID domainId, @RequestBody Vote vote) throws ResourceNotFoundException {
        return voteService.createVote(domainId, vote);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public Vote updateVote(@RequestBody Vote vote) throws ResourceNotFoundException {
        return voteService.updateVote(vote);
    }

    @DeleteMapping("/{voteId}")
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public void deleteVote(@PathVariable(value = "voteId") UUID voteId) throws ResourceNotFoundException {
        voteService.deleteVote(voteId);
    }
}
