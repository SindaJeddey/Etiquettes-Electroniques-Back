package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.inStoreProduct.InStoreProductDTOToInStoreProductConverter;
import project.ee.dto.inStoreProduct.InStoreProductToInStoreProductDTOConverter;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.movement.MovementDTOToMovementConverter;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.dto.store.StoreDTO;
import project.ee.dto.store.StoreDTOToStoreConverter;
import project.ee.dto.store.StoreToStoreDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.exceptions.ResourceNotValidException;
import project.ee.models.models.*;
import project.ee.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreService {


    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InStoreProductRepository inStoreProductRepository;
    private final StoreDTOToStoreConverter toStoreConverter;
    private final StoreToStoreDTOConverter toStoreDTOConverter;
    private final InStoreProductDTOToInStoreProductConverter toInStoreProductConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;
    private final InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter;
    private final MovementDTOToMovementConverter toMovementConverter;

    public StoreService(StoreRepository storeRepository,
                        ProductRepository productRepository,
                        InStoreProductRepository inStoreProductRepository,
                        StoreDTOToStoreConverter toStoreConverter,
                        StoreToStoreDTOConverter toStoreDTOConverter,
                        InStoreProductDTOToInStoreProductConverter toInStoreProductConverter,
                        ProductToProductDTOConverter toProductDTOConverter,
                        InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter,
                        MovementDTOToMovementConverter toMovementConverter) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inStoreProductRepository = inStoreProductRepository;
        this.toStoreConverter = toStoreConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
        this.toInStoreProductConverter = toInStoreProductConverter;
        this.toProductDTOConverter = toProductDTOConverter;
        this.toInStoreProductDTOConverter = toInStoreProductDTOConverter;
        this.toMovementConverter = toMovementConverter;
    }

    private Store fetchStore(String  id) throws ResourceNotFoundException {
        return storeRepository.findByStoreCode(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store id: " + id + " not found"));
    }

    public Store save(Store store){
        return storeRepository.save(store);
    }

    public List<StoreDTO> getAllStores(){
        return storeRepository.findAll()
                .stream()
                .map(toStoreDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public StoreDTO getStore(String id) throws ResourceNotFoundException {
        Store store = fetchStore(id);
        return toStoreDTOConverter.convert(store);
    }

    public StoreDTO addStore(StoreDTO storeDTO) {
        Store toSave = toStoreConverter.convert(storeDTO);
        Store saved = storeRepository.save(toSave);
        toSave.setStoreCode(RandomStringUtils.randomAlphabetic(5));
        return toStoreDTOConverter.convert(saved);
    }

    public void deleteStore(String id) throws ResourceNotFoundException {
        Store store = fetchStore(id);
        storeRepository.delete(store);
    }

    public StoreDTO updateStore(String id, StoreDTO storeDTO) throws ResourceNotFoundException {
        Store store = fetchStore(id);
        if(storeDTO.getName() != null)
            store.setName(storeDTO.getName());
        if(storeDTO.getLocation() != null)
            store.setLocation(storeDTO.getLocation());
        if(storeDTO.getZipCode() != null)
            store.setZipCode(storeDTO.getZipCode());
        Store saved = storeRepository.save(store);
        return toStoreDTOConverter.convert(saved);
    }

    public InStoreProductDTO addProduct(String storeId, MovementDTO movementDTO) throws ResourceNotFoundException {
        if (!movementDTO.getType().equals(MovementType.IN.name()))
            throw new ResourceNotValidException("Invalid Transaction");

        if (!movementDTO.getProduct().getStore().getStoreCode().equals(storeId))
            throw new ResourceNotFoundException("Incompatible stores");

        Store store = fetchStore(storeId);
        InStoreProduct inStoreProduct = toInStoreProductConverter.convert(movementDTO.getProduct());
        if(inStoreProduct.getProduct() == null ||
                inStoreProduct.getProduct().getProductCode() == null ||
                inStoreProduct.getProduct().getProductCode().isEmpty() )
            throw new ResourceNotFoundException("Must provide product to add to store");

        Optional<Product> product = productRepository.findByProductCode(inStoreProduct.getProduct().getProductCode());
        if(!product.isPresent())
            throw new ResourceNotFoundException("Product code:"+inStoreProduct.getProduct().getProductCode()+"non existing");

        boolean productExisting = store.getInStoreProducts()
                .stream()
                .anyMatch(inStoreProduct1 ->
                        inStoreProduct1.getProduct().equals(inStoreProduct.getProduct()));

        if(productExisting) {
            throw new ResourceNotValidException("Product already exists in store");
        }

        inStoreProduct.setStore(store);
        inStoreProduct.setProduct(product.get());
        inStoreProduct.setInStoreProductCode(RandomStringUtils.randomAlphabetic(10));
        inStoreProduct.setQuantity(movementDTO.getQuantity());
        if (inStoreProduct.getQuantity() <= inStoreProduct.getProduct().getQuantityThreshold()) {
            inStoreProduct.setAlertThreshold(true);
        } else {
            inStoreProduct.setAlertThreshold(false);
        }
        Movement movement = toMovementConverter.convert(movementDTO);
        movement.setMovementDate(LocalDate.now());
        movement.setProduct(inStoreProduct);
        movement.setMovementCode(RandomStringUtils.randomAlphabetic(10));
        inStoreProduct.addMovement(movement);

        Tag tag = Tag.builder()
                .product(inStoreProduct)
                .build();
        inStoreProduct.setTag(tag);
        tag.setProduct(inStoreProduct);
        store.addInStoreProduct(inStoreProduct);

        storeRepository.save(store);

        return toInStoreProductDTOConverter.convert(inStoreProduct);
    }

    public InStoreProductDTO removeProduct(String storeId, MovementDTO movementDTO) throws ResourceNotFoundException {
        if (!movementDTO.getType().equals(MovementType.OUT.name()))
            throw new ResourceNotValidException("Invalid Transaction");

        if (!movementDTO.getProduct().getStore().getStoreCode().equals(storeId))
            throw new ResourceNotValidException("Incompatible stores");

        Store store = fetchStore(storeId);
        if(movementDTO.getProduct() == null ) {
            throw new ResourceNotValidException("Must provide product to remove from store");
        }

        InStoreProduct inStoreProduct = toInStoreProductConverter.convert(movementDTO.getProduct());
        InStoreProduct finalInStoreProduct = inStoreProductRepository.findByInStoreProductCode(inStoreProduct.getInStoreProductCode())
                .orElseThrow(()-> new ResourceNotFoundException("In store product id:"+inStoreProduct.getId()+" not found"));
        store.removeInStoreProduct(finalInStoreProduct);
        Product product = finalInStoreProduct.getProduct();
        product.removeInStoreProduct(finalInStoreProduct);
        inStoreProductRepository.delete(finalInStoreProduct);
        productRepository.save(product);
        storeRepository.save(store);

        return toInStoreProductDTOConverter.convert(inStoreProduct);
    }

    public StoreDTO fetchInStoreProducts(String id) throws ResourceNotFoundException {
        Store store = fetchStore(id);
        Set<InStoreProductDTO> productDTOS = store.getInStoreProducts()
                .stream()
                .map(toInStoreProductDTOConverter::convert)
                .collect(Collectors.toSet());
        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setInStoreProducts(productDTOS);
        return dto;
    }

    public StoreDTO fetchCategoryInStoreProducts(String id, String categoryId) throws ResourceNotFoundException {
        Store store = fetchStore(id);
        Set<InStoreProductDTO> productDTOS = store.getInStoreProducts()
                .stream()
                .filter(inStoreProduct -> inStoreProduct.getCategory().getCategoryCode().equals(categoryId))
                .map(toInStoreProductDTOConverter::convert)
                .collect(Collectors.toSet());
        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setInStoreProducts(productDTOS);
        return dto;
    }

    public Set<String> getAllLocations() {
        return storeRepository.findAll()
                .stream()   
                .map(Store::getLocation)
                .collect(Collectors.toSet());
    }

    public List<StoreDTO> getAllStoresByLocation(String location) {
        return storeRepository.findAllByLocation(location)
                .stream()
                .map(toStoreDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public Set<ProductDTO> getBelowThreshold(String id) throws ResourceNotFoundException {
        Store store = fetchStore(id);
        return store.getInStoreProducts()
                .stream().filter(InStoreProduct::isAlertThreshold)
                .map(inStoreProduct -> toProductDTOConverter.convert(inStoreProduct.getProduct()))
                .collect(Collectors.toSet());
    }

    public InStoreProductDTO fetchInStoreProduct(String storeId, String productId) throws ResourceNotFoundException {
        Store store = fetchStore(storeId);
        Optional<InStoreProduct> inStoreProduct = store.getInStoreProducts()
                .stream()
                .filter(inStoreProduct1 -> inStoreProduct1.getInStoreProductCode().equals(productId) ||
                        inStoreProduct1.getProduct().getProductCode().equals(productId))
                .findFirst();
        if(inStoreProduct.isPresent())
            return toInStoreProductDTOConverter.convert(inStoreProduct.get());
        else {
            throw new ResourceNotFoundException("Product not in store");
        }
    }
}

