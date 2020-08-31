package project.ee.services;

import org.springframework.stereotype.Service;
import project.ee.dto.category.CategoryDTO;
import project.ee.dto.category.CategoryDTOToCategoryConverter;
import project.ee.dto.category.CategoryToCategoryDTOConverter;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Category;
import project.ee.repositories.CategoryRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDTOConverter toCategoryDTOConverter;
    private final CategoryDTOToCategoryConverter toCategoryConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryToCategoryDTOConverter toCategoryDTOConverter,
                           CategoryDTOToCategoryConverter toCategoryConverter,
                           ProductToProductDTOConverter toProductDTOConverter) {
        this.categoryRepository = categoryRepository;
        this.toCategoryDTOConverter = toCategoryDTOConverter;
        this.toCategoryConverter = toCategoryConverter;
        this.toProductDTOConverter = toProductDTOConverter;
    }

    public Category saveCategory (Category category){
        return categoryRepository.save(category);
    }

    public CategoryDTO save(CategoryDTO categoryDTO){
        Category toSave= toCategoryConverter.convert(categoryDTO);
        Category saved = categoryRepository.save(toSave);
        return toCategoryDTOConverter.convert(saved);
    }

    public List<CategoryDTO> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(toCategoryDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategory(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        return toCategoryDTOConverter.convert(category);
    }

    public void deleteCategory(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        categoryRepository.delete(category);
    }

    public CategoryDTO getProducts(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        Set<ProductDTO> dtoSet = category.getProducts()
                .stream()
                .map(toProductDTOConverter::convert)
                .collect(Collectors.toSet());
        CategoryDTO categoryDTO = toCategoryDTOConverter.convert(category);
        categoryDTO.setProducts(dtoSet);
        return categoryDTO;
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        Category updates = toCategoryConverter.convert(categoryDTO);
        if(updates.getName() != null)
            category.setName(updates.getName());
        Category saved = categoryRepository.save(category);
        return toCategoryDTOConverter.convert(saved);
    }
}
