package space.netbytes.adshunters.services.domains;

import space.netbytes.adshunters.converters.Converter;
import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.domains.Category;
import space.netbytes.adshunters.models.domains.Domain;
import space.netbytes.adshunters.repositories.domains.CategoryRepository;
import space.netbytes.adshunters.repositories.domains.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category getCategory(UUID categoryId) throws ResourceNotFoundException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + categoryId));
    }

    public Category createCategory(Category category) {
        category.setCreatedAt(new Date());
        return categoryRepository.save(category);
    }

    public Category updateCategoryDetails(Category newCategory) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(newCategory.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + newCategory.getId()));
        Converter.copyNotNullProperties(newCategory, category);
        return categoryRepository.save(category);
    }

    public Category addDomainToCategory(UUID categoryId, UUID domainId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + categoryId));
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + domainId));
        List<Domain> domains = category.getDomains();
        if (!domains.contains(domain)) {
            domains.add(domain);
            category.setDomains(domains);
            return categoryRepository.save(category);
        }
        return category;
    }

    public Category removeDomainFromCategory(UUID categoryId, UUID domainId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + categoryId));
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found for this uuid :: " + domainId));
        List<Domain> domains = category.getDomains();
        if (domains.contains(domain)) {
            domains.remove(domain);
            category.setDomains(domains);
            return categoryRepository.save(category);
        }
        return category;
    }

    public Category updateDomainsForCategory(UUID categoryId, List<Domain> domains) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this uuid :: " + categoryId));
        category.setDomains(domains);
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID uuid) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Domain category not found for this uuid :: " + uuid));
        categoryRepository.delete(category);
    }
}
