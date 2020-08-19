package project.ee.services;

import org.springframework.stereotype.Service;
import project.ee.dto.category.CategoryDTO;
import project.ee.dto.category.CategoryToCategoryDTOConverter;
import project.ee.dto.product.ProductDTO;
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
    private final ProductToProductDTOConverter toProductDTOConverter;


    public StoreService(StoreRepository storeRepository,
                        CategoryRepository categoryRepository,
                        ProductRepository productRepository,
                        StoreDTOToStoreConverter toStoreConverter,
                        StoreToStoreDTOConverter toStoreDTOConverter,
                        CategoryToCategoryDTOConverter toCategoryDTOConverter,
                        ProductToProductDTOConverter toProductDTOConverter) {
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.toStoreConverter = toStoreConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
        this.toCategoryDTOConverter = toCategoryDTOConverter;
        this.toProductDTOConverter = toProductDTOConverter;
    }

    public Store save(Store store){
        return storeRepository.save(store);
    }

    public List<StoreDTO> getAllStores(){
        List<StoreDTO> dtos = storeRepository.findAll()
                .stream()
                .map(store -> toStoreDTOConverter.convert(store))
                .collect(Collectors.toList());
        return dtos;
    }

    public StoreDTO getStore(Long id) throws NotFoundException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));
        StoreDTO dto = toStoreDTOConverter.convert(store);
        return dto;
    }

    public StoreDTO addStore(StoreDTO storeDTO) {
        Store toSave = toStoreConverter.convert(storeDTO);
        Store saved = storeRepository.save(toSave);
        StoreDTO savedDto = toStoreDTOConverter.convert(saved);
        return savedDto;
    }

    public void deleteStore(Long id) throws NotFoundException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

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


    public StoreDTO addCategory(Long id, Long choiceId) throws NotFoundException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

        Category category = categoryRepository.findById(choiceId)
                .orElseThrow(() -> new NotFoundException("Category id: "+choiceId+" not found"));

        Set<Category> categories = store.getCategories();
        categories.add(category);
        store.setCategories(categories);
        Store saved = storeRepository.save(store);

        Set<Store> stores = category.getStores();
        stores.add(saved);
        category.setStores(stores);
        categoryRepository.save(category);

        StoreDTO savedDTO = toStoreDTOConverter.convert(saved);
        return savedDTO;
    }

    public StoreDTO addProduct(Long id, Long choiceId) throws NotFoundException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

        Product product = productRepository.findById(choiceId)
                .orElseThrow(() -> new NotFoundException("Product id: "+choiceId+" not found"));

        Category category = product.getCategory();
        boolean categoryExists = store.getCategories().stream().anyMatch(category1 -> category1.equals(category));
        if(!categoryExists)
            throw new RuntimeException("Store "+id+" doesn't have category "+category.getId()+". Can't add product");

        Set<Product> products = store.getProducts();
        products.add(product);
        store.setProducts(products);
        Store saved = storeRepository.save(store);

        Set<Store> stores =product.getStores();
        stores.add(saved);
        product.setStores(stores);
        productRepository.save(product);

        StoreDTO savedDTO = toStoreDTOConverter.convert(saved);
        return savedDTO;
    }

    public StoreDTO removeCategory(Long id, Long categoryId) throws NotFoundException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

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
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

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
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

        Set<CategoryDTO> categoryDTOS = store.getCategories()
                .stream()
                .map(category -> toCategoryDTOConverter.convert(category))
                .collect(Collectors.toSet());

        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setCategories(categoryDTOS);

        return dto;
    }

    public StoreDTO fetchProducts(Long id) throws NotFoundException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store id: "+id+" not found"));

        Set<ProductDTO> productDTOS = store.getProducts()
                .stream()
                .map(product -> toProductDTOConverter.convert(product))
                .collect(Collectors.toSet());

        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setProducts(productDTOS);

        return dto;

    }
}
