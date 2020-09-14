package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.category.CategoryDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("permitAll()")
//@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN')")
public class CategoryController {
    //All good in here

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    CategoryDTO getCategory(@PathVariable String id) throws NotFoundException {
        return categoryService.getCategory(id);
    }

    @GetMapping("/names")
    List<String> getCategoriesName() {
        return categoryService.getAllCategoriesNames();
    }

    @PostMapping("/new")
    public CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO){
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

