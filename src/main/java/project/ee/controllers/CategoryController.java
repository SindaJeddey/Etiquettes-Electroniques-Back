package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.category.CategoryDTO;
import project.ee.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping(CategoryController.BASE_URI)
@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN')")
public class CategoryController {

    public static final String BASE_URI = "/api/categories";

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_OPERATOR','ROLE_OPERATOR','ROLE_ADMIN')")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
   @PreAuthorize("hasAnyAuthority('ROLE_SUPER_OPERATOR','ROLE_OPERATOR','ROLE_ADMIN')")
    public CategoryDTO getCategory(@PathVariable String id){
        return categoryService.getCategory(id);
    }

    @PostMapping()
    public CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.save(categoryDTO);
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory (@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id,categoryDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String  id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}/products")
    public CategoryDTO getAllProducts(@PathVariable String id){
        return categoryService.getProducts(id);
    }

}

