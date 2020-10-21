package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.category.CategoryDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.services.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
//@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_OPERATOR','ROLE_OPERATOR','ROLE_ADMIN')")
    List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_OPERATOR','ROLE_OPERATOR','ROLE_ADMIN')")
    CategoryDTO getCategory(@PathVariable String id) throws NotFoundException {
        return categoryService.getCategory(id);
    }

    @GetMapping("/names")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_OPERATOR','ROLE_OPERATOR','ROLE_ADMIN')")
    List<String> getCategoriesName() {
        return categoryService.getAllCategoriesNames();
    }

    @PostMapping("/new")
    public CategoryDTO addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        if(categoryDTO == null)
            throw new IllegalArgumentException("Must provide a category to save");
        return categoryService.save(categoryDTO);
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory (@PathVariable String id, @RequestBody CategoryDTO categoryDTO) throws NotFoundException {
        return categoryService.updateCategory(id,categoryDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String  id) throws NotFoundException {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}/products")
    public CategoryDTO getAllProducts(@PathVariable String id) throws NotFoundException {
        return categoryService.getProducts(id);
    }

}

