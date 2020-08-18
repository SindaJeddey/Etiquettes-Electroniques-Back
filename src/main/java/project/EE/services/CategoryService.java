package project.EE.services;

import org.springframework.stereotype.Service;
import project.EE.dto.category.CategoryDTO;
import project.EE.dto.category.CategoryDTOToCategoryConverter;
import project.EE.dto.category.CategoryToCategoryDTOConverter;
import project.EE.dto.product.ProductDTO;
import project.EE.dto.product.ProductToProductDTOConverter;
import project.EE.dto.store.StoreDTO;
import project.EE.dto.store.StoreToStoreDTOConverter;
import project.EE.exceptions.NotFoundException;
import project.EE.models.models.Category;
import project.EE.repositories.CategoryRepository;
import project.EE.repositories.ProductRepository;
import project.EE.repositories.StoreRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryToCategoryDTOConverter toCategoryDTOConverter;
    private final CategoryDTOToCategoryConverter toCategoryConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;
    private final StoreToStoreDTOConverter toStoreDTOConverter;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           StoreRepository storeRepository,
                           CategoryToCategoryDTOConverter toCategoryDTOConverter,
                           CategoryDTOToCategoryConverter toCategoryConverter,
                           ProductToProductDTOConverter toProductDTOConverter,
                           StoreToStoreDTOConverter toStoreDTOConverter) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.toCategoryDTOConverter = toCategoryDTOConverter;
        this.toCategoryConverter = toCategoryConverter;
        this.toProductDTOConverter = toProductDTOConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
    }

    public Category saveCategory (Category category){
        return categoryRepository.save(category);
    }

    public CategoryDTO save(CategoryDTO categoryDTO){
        Category toSave= toCategoryConverter.convert(categoryDTO);
        toSave.setStores(null);
        toSave.setProductSet(null);
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

    public CategoryDTO getCategory(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        CategoryDTO dto = toCategoryDTOConverter.convert(category);
        return dto;
    }

    public void deleteCategory(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        category.getStores().forEach(store -> {
            store.getCategories().remove(category);
            storeRepository.save(store);
        });
        category.setStores(null);
        categoryRepository.delete(category);
    }

    public CategoryDTO getProducts(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        Set<ProductDTO> dtoSet = category.getProductSet()
                .stream()
                .map(product -> toProductDTOConverter.convert(product))
                .collect(Collectors.toSet());

        CategoryDTO categoryDTO = toCategoryDTOConverter.convert(category);
        categoryDTO.setProductSet(dtoSet);

        return categoryDTO;
    }

    public CategoryDTO getStores(Long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: "+id+" not found"));
        Set<StoreDTO> dtoSet = category.getStores()
                .stream()
                .map(store -> toStoreDTOConverter.convert(store))
                .collect(Collectors.toSet());

        CategoryDTO categoryDTO = toCategoryDTOConverter.convert(category);
        categoryDTO.setStoreSet(dtoSet);

        return categoryDTO;
    }
}
