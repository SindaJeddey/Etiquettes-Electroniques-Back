package project.EE.controllers;

import org.springframework.web.bind.annotation.*;
import project.EE.dto.category.CategoryDTO;
import project.EE.exceptions.NotFoundException;
import project.EE.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
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
}
