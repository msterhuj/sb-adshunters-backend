package space.netbytes.adshunters.services.domains;

import space.netbytes.adshunters.converters.Converter;
import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.domains.Domain;
import space.netbytes.adshunters.models.domains.Category;
import space.netbytes.adshunters.repositories.domains.CategoryRepository;
import space.netbytes.adshunters.repositories.domains.DomainRepository;
import space.netbytes.adshunters.services.KeycloakService;
import space.netbytes.adshunters.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private KeycloakService keycloakService;


    public List<Domain> getAllDomain() {
        return (List<Domain>) domainRepository.findAll();
    }

    public Domain getDomain(UUID domainId) throws ResourceNotFoundException {
        return domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + domainId));
    }

    public Domain createDomain(Domain domain) throws ResourceNotFoundException {
        domain.setCreatedAt(new Date());
        domain.setUser(userDetailsService.getUserDetails(keycloakService.getUUID()));
        return domainRepository.save(domain);
    }

    public Domain updateDomainDetails(Domain newDomain) throws ResourceNotFoundException {
        Domain domain = domainRepository.findById(newDomain.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + newDomain.getId()));
        Converter.copyNotNullProperties(newDomain, domain);
        return domainRepository.save(domain);
    }

    public Domain addCategoryToDomain(UUID categoryId, UUID domainId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + categoryId));
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + domainId));
        List<Category> categories = domain.getCategories();
        if (!categories.contains(category)) {
            categories.add(category);
            domain.setCategories(categories);
            return domainRepository.save(domain);
        }
        return domain;
    }

    public Domain removeCategoryFromDomain(UUID categoryId, UUID domainId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + categoryId));
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + domainId));
        List<Category> categories = domain.getCategories();
        if (categories.contains(category)) {
            categories.remove(category);
            domain.setCategories(categories);
            return domainRepository.save(domain);
        }
        return domain;
    }

    public void deleteDomain(UUID uuid) throws ResourceNotFoundException {
        Domain domain = domainRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + uuid));
        domainRepository.delete(domain);
    }
}
