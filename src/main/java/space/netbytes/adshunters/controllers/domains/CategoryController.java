package space.netbytes.adshunters.controllers.domains;

import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.domains.Category;
import space.netbytes.adshunters.models.domains.Domain;
import space.netbytes.adshunters.services.domains.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/{categoryId}")
    public Category getCategory(@PathVariable(value = "categoryId") UUID categoryId) throws ResourceNotFoundException {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public Category createCategory(@RequestBody @Valid Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public Category updateCategoryDetails(@RequestBody Category category) throws ResourceNotFoundException {
        return categoryService.updateCategoryDetails(category);
    }

    @PostMapping("/{categoryId}/{domainId}")
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public Category addDomainToCategory(@PathVariable(value = "categoryId") UUID categoryId, @PathVariable(value = "domainId") UUID domainId) throws ResourceNotFoundException {
        return categoryService.addDomainToCategory(categoryId, domainId);
    }

    @DeleteMapping("/{categoryId}/{domainId}")
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public Category removeDomainFromCategory(@PathVariable(value = "categoryId") UUID categoryId, @PathVariable(value = "domainId") UUID domainId) throws ResourceNotFoundException {
        return categoryService.removeDomainFromCategory(categoryId, domainId);
    }

    @PostMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public Category updateCategoryDomain(@PathVariable(value = "categoryId") UUID categoryId, @RequestBody List<Domain> domains) throws ResourceNotFoundException {
        return categoryService.updateDomainsForCategory(categoryId, domains);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADSHUNTERS_ADMIN')")
    public void deleteCategory(@PathVariable(value = "categoryId") UUID categoryId) throws ResourceNotFoundException {
        categoryService.deleteCategory(categoryId);
    }
}
