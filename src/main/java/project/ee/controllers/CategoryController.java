package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.category.CategoryDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("hasAuthority('ROLE_OPERATOR')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    CategoryDTO getCategory(@PathVariable Long id) throws NotFoundException {
        return categoryService.getCategory(id);
    }

    @PostMapping("/new")
    public CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO){
        if(categoryDTO == null)
            throw new IllegalArgumentException("Must provide a category to save");
        return categoryService.save(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) throws NotFoundException {
        categoryService.deleteCategory(id);
    }


    @GetMapping("/{id}/{choice}")
    public CategoryDTO getChoices(@PathVariable Long id,
                                   @PathVariable String choice) throws NotFoundException {
        switch (choice){
            case "stores":{
                return categoryService.getStores(id);
            }
            case "products":{
                return categoryService.getProducts(id);
            }
            default:
                throw new RuntimeException("Choice invalid");
        }
    }
}
