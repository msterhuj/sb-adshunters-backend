package space.netbytes.adshunters.controllers.domains;

import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.domains.Domain;
import space.netbytes.adshunters.services.domains.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/domains")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @GetMapping
    public List<Domain> getAllDomains() {
        return domainService.getAllDomain();
    }

    @GetMapping("/{domainId}")
    public Domain getDomain(@PathVariable(value = "domainId") UUID domainId) throws ResourceNotFoundException {
        return domainService.getDomain(domainId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public Domain createDomain(@RequestBody Domain domain) throws ResourceNotFoundException {
        return domainService.createDomain(domain);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public Domain updateDomainDetails(@RequestBody Domain domain) throws ResourceNotFoundException {
        return domainService.updateDomainDetails(domain);
    }

    @PostMapping("/{domainId}/{categoryId}")
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public Domain addCategoryToDomain(@PathVariable(value = "domainId") UUID domainId, @PathVariable(value = "categoryId") UUID categoryId) throws ResourceNotFoundException {
        return domainService.addCategoryToDomain(categoryId, domainId);
    }

    @DeleteMapping("/{domainId}/{categoryId}")
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public Domain removeCategoryFromDomain(@PathVariable(value = "domainId") UUID domainId, @PathVariable(value = "categoryId") UUID categoryId) throws ResourceNotFoundException {
        return domainService.removeCategoryFromDomain(categoryId, domainId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public void deleteDomain(@PathVariable(value = "id") UUID uuid) throws ResourceNotFoundException {
        domainService.deleteDomain(uuid);
    }
}
