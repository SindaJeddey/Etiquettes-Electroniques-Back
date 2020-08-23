package project.ee.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.ee.dto.category.CategoryDTO;
import project.ee.dto.category.CategoryDTOToCategoryConverter;
import project.ee.dto.category.CategoryToCategoryDTOConverter;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.dto.store.StoreDTO;
import project.ee.dto.store.StoreDTOToStoreConverter;
import project.ee.dto.store.StoreToStoreDTOConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.models.models.Store;
import project.ee.repositories.CategoryRepository;
import project.ee.repositories.ProductRepository;
import project.ee.repositories.StoreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StoreService {


    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StoreDTOToStoreConverter toStoreConverter;
    private final StoreToStoreDTOConverter toStoreDTOConverter;
    private final CategoryToCategoryDTOConverter toCategoryDTOConverter;
    private final CategoryDTOToCategoryConverter toCategoryConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;
    private final ProductDTOToProductConverter toProductConverter;

    public StoreService(StoreRepository storeRepository,
                        CategoryRepository categoryRepository,
                        ProductRepository productRepository,
                        StoreDTOToStoreConverter toStoreConverter,
                        StoreToStoreDTOConverter toStoreDTOConverter,
                        CategoryToCategoryDTOConverter toCategoryDTOConverter,
                        CategoryDTOToCategoryConverter toCategoryConverter,
                        ProductToProductDTOConverter toProductDTOConverter,
                        ProductDTOToProductConverter toProductConverter) {
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.toStoreConverter = toStoreConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
        this.toCategoryDTOConverter = toCategoryDTOConverter;
        this.toCategoryConverter = toCategoryConverter;
        this.toProductDTOConverter = toProductDTOConverter;
        this.toProductConverter = toProductConverter;
    }

    private Store fetchStore(Long id) throws NotFoundException {
        return storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: " + id + " not found"));
    }

    public Store save(Store store){
        return storeRepository.save(store);
    }

    public List<StoreDTO> getAllStores(){
        return storeRepository.findAll()
                .stream()
                .map(store -> toStoreDTOConverter.convert(store))
                .collect(Collectors.toList());
    }

    public StoreDTO getStore(Long id) throws NotFoundException {
        Store store = fetchStore(id);
        return toStoreDTOConverter.convert(store);
    }

    public StoreDTO addStore(StoreDTO storeDTO) {
        Store toSave = toStoreConverter.convert(storeDTO);
        Store saved = storeRepository.save(toSave);
        StoreDTO savedDto = toStoreDTOConverter.convert(saved);
        return savedDto;
    }

    public void deleteStore(Long id) throws NotFoundException {
        Store store = fetchStore(id);

        //Deleting store from product
        Set<Product> products = store.getProducts();
        products.forEach(product -> {
            Set<Store> stores = product.getStores();
            stores.remove(store);
            product.setStores(stores);
            productRepository.save(product);
        });

        //Deleting store from categories
        Set<Category> categories = store.getCategories();
        categories.forEach(category -> {
            Set<Store> stores = category.getStores();
            stores.remove(store);
            category.setStores(stores);
            categoryRepository.save(category);
        });
        storeRepository.delete(store);
    }


    public StoreDTO updateStore(Long id, StoreDTO storeDTO) throws NotFoundException {
        Store store = fetchStore(id);
        if(storeDTO.getName() != null)
            store.setName(storeDTO.getName());
        if(storeDTO.getLocation() != null)
            store.setLocation(storeDTO.getLocation());
        if(storeDTO.getZipCode() != null)
            store.setZipCode(storeDTO.getZipCode());
        if(storeDTO.getCategories() != null) {
            //to refactor to optimize
            store.setCategories(new HashSet<>());
            Set<Category> cats = storeDTO.getCategories()
                    .stream()
                    .map(categoryDTO -> toCategoryConverter.convert(categoryDTO))
                    .collect(Collectors.toSet());
            cats.forEach(category -> {
                        try {
                            addCategory(store, category.getId());
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            store.setCategories(cats);
        }

        if(storeDTO.getProducts() != null) {
            //to refactor to optimize
            storeDTO.getProducts()
                    .stream()
                    .map(productDTO -> toProductConverter.convert(productDTO))
                    .forEach(product -> {
                        try {
                            addProduct(store,product.getId());
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        }
        Store saved = storeRepository.save(store);
        return toStoreDTOConverter.convert(saved);
    }

    private void addCategory(Store store, Long choiceId) throws NotFoundException {
        Category category = categoryRepository.findById(choiceId)
                .orElseThrow(() -> new NotFoundException("Category id: "+choiceId+" not found"));
        Set<Category> categories = store.getCategories();
        if(!categories.contains(category)) {
            categories.add(category);
            store.setCategories(categories);
            Store saved = storeRepository.save(store);

            Set<Store> stores = category.getStores();
            stores.add(saved);
            category.setStores(stores);
            categoryRepository.save(category);
        }
    }


    private void addProduct(Store store, Long choiceId) throws NotFoundException {
        Product product = productRepository.findById(choiceId)
                .orElseThrow(() -> new NotFoundException("Product id: "+choiceId+" not found"));

        Category category = product.getCategory();
        boolean categoryExists = store.getCategories().stream().anyMatch(category1 -> category1.equals(category));
        if(!categoryExists)
            throw new RuntimeException("Store "+store.getId()+" doesn't have category "+category.getId()+". Can't add product");

        Set<Product> products = store.getProducts();
        if(!products.contains(product)) {
            products.add(product);
            store.setProducts(products);
            Store saved = storeRepository.save(store);

            Set<Store> stores = product.getStores();
            stores.add(saved);
            product.setStores(stores);
            productRepository.save(product);
        }
    }

    public StoreDTO removeCategory(Long id, Long categoryId) throws NotFoundException {
        Store store = fetchStore(id);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category id: "+categoryId+" not found"));

        Set<Category> categories = store.getCategories();
        if(categories.contains(category)){
            //Removing category from store
            categories.remove(category);
            store.setCategories(categories);

            //Removing products of the category from store
            Set<Product> products = store.getProducts();
            Set<Product> newProducts = new HashSet<>();
            products.forEach(product -> {
                if(product.getCategory().equals(category)){
                    Set<Store> stores = product.getStores();
                    stores.remove(store);
                    productRepository.save(product);
                }
                else
                    newProducts.add(product);
            });
            store.setProducts(newProducts);

            Store saved = storeRepository.save(store);

            Set<Store> stores = category.getStores();
            stores.remove(saved);
            category.setStores(stores);
            categoryRepository.save(category);

            return toStoreDTOConverter.convert(saved);
        }
        return toStoreDTOConverter.convert(store);
    }

    public StoreDTO removeProduct(Long id, Long productId) throws NotFoundException {
        Store store = fetchStore(id);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product id: "+productId+" not found"));

        Category category = product.getCategory();
        if(!store.getCategories().contains(category))
            return toStoreDTOConverter.convert(store);

        if(store.getProducts().contains(product)){
            Set<Product> products = store.getProducts();
            products.remove(product);
            store.setProducts(products);
            Store saved = storeRepository.save(store);

            Set<Store> stores = product.getStores();
            stores.remove(saved);
            product.setStores(stores);
            productRepository.save(product);

            return toStoreDTOConverter.convert(saved);
        }
        return toStoreDTOConverter.convert(store);
    }

    public StoreDTO fetchCategories(Long id) throws NotFoundException {
        Store store = fetchStore(id);

        Set<CategoryDTO> categoryDTOS = store.getCategories()
                .stream()
                .map(category -> toCategoryDTOConverter.convert(category))
                .collect(Collectors.toSet());

        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setCategories(categoryDTOS);

        return dto;
    }

    public StoreDTO fetchProducts(Long id) throws NotFoundException {
        Store store = fetchStore(id);

        Set<ProductDTO> productDTOS = store.getProducts()
                .stream()
                .map(product -> toProductDTOConverter.convert(product))
                .collect(Collectors.toSet());

        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setProducts(productDTOS);

        return dto;

    }
}
