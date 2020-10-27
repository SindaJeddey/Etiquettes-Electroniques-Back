package project.ee.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import project.ee.dto.category.CategoryDTO;
import project.ee.dto.category.CategoryDTOToCategoryConverter;
import project.ee.dto.category.CategoryToCategoryDTOConverter;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
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

    private static final String NOT_FOUND = "Category with id: %s not found";

    public Category saveCategory (Category category){
        return categoryRepository.save(category);
    }

    public CategoryDTO save(CategoryDTO categoryDTO){
        Category toSave= toCategoryConverter.convert(categoryDTO);
        toSave.setCategoryCode(RandomStringUtils.randomAlphabetic(5));
        Category saved = categoryRepository.save(toSave);
        return toCategoryDTOConverter.convert(saved);
    }

    public List<CategoryDTO> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(toCategoryDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategory(String id) throws ResourceNotFoundException {
        Category category = categoryRepository.findByCategoryCode(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND,id)));
        return toCategoryDTOConverter.convert(category);
    }

    public void deleteCategory(String id) throws ResourceNotFoundException {
        Category category = categoryRepository.findByCategoryCode(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND,id)));
        categoryRepository.delete(category);
    }

    public CategoryDTO getProducts(String id) throws ResourceNotFoundException {
        Category category = categoryRepository.findByCategoryCode(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND,id)));
        Set<ProductDTO> dtoSet = category.getProducts()
                .stream()
                .map(toProductDTOConverter::convert)
                .collect(Collectors.toSet());
        CategoryDTO categoryDTO = toCategoryDTOConverter.convert(category);
        categoryDTO.setProducts(dtoSet);
        return categoryDTO;
    }

    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) throws ResourceNotFoundException {
        Category category = categoryRepository.findByCategoryCode(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND,id)));
        if(categoryDTO.getName() != null)
            category.setName(categoryDTO.getName());
        Category saved = categoryRepository.save(category);
        return toCategoryDTOConverter.convert(saved);
    }
}
