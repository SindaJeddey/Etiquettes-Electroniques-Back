package project.EE.services;

import org.springframework.stereotype.Service;
import project.EE.dto.category.CategoryDTO;
import project.EE.dto.category.CategoryDTOToCategoryConverter;
import project.EE.dto.category.CategoryToCategoryDTOConverter;
import project.EE.exceptions.NotFoundException;
import project.EE.models.Category;
import project.EE.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDTOConverter toCategoryDTOConverter;
    private final CategoryDTOToCategoryConverter toCategoryConverter;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryToCategoryDTOConverter toCategoryDTOConverter,
                           CategoryDTOToCategoryConverter toCategoryConverter) {
        this.categoryRepository = categoryRepository;
        this.toCategoryDTOConverter = toCategoryDTOConverter;
        this.toCategoryConverter = toCategoryConverter;
    }

    public Category saveCategory (Category category){
        return categoryRepository.save(category);
    }

    public CategoryDTO save(CategoryDTO categoryDTO){
        Category toSave= toCategoryConverter.convert(categoryDTO);
        Category saved = categoryRepository.save(toSave);
        CategoryDTO savedDto = toCategoryDTOConverter.convert(saved);
        return savedDto;
    }

    public List<CategoryDTO> getAllCategories(){
        List<CategoryDTO> dtos = categoryRepository.findAll()
                .stream()
                .map(category -> toCategoryDTOConverter.convert(category))
                .collect(Collectors.toList());
        return dtos;
    }

    public void deleteCategory(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        categoryRepository.delete(category);
    }

}
