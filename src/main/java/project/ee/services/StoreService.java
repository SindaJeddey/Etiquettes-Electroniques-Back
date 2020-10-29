package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.inStoreProduct.InStoreProductDTOToInStoreProductConverter;
import project.ee.dto.inStoreProduct.InStoreProductToInStoreProductDTOConverter;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.movement.MovementDTOToMovementConverter;
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
    private final InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter;
    private final MovementDTOToMovementConverter toMovementConverter;

    public StoreService(StoreRepository storeRepository,
                        ProductRepository productRepository,
                        InStoreProductRepository inStoreProductRepository,
                        StoreDTOToStoreConverter toStoreConverter,
                        StoreToStoreDTOConverter toStoreDTOConverter,
                        InStoreProductDTOToInStoreProductConverter toInStoreProductConverter,
                        InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter,
                        MovementDTOToMovementConverter toMovementConverter) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inStoreProductRepository = inStoreProductRepository;
        this.toStoreConverter = toStoreConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
        this.toInStoreProductConverter = toInStoreProductConverter;
        this.toInStoreProductDTOConverter = toInStoreProductDTOConverter;
        this.toMovementConverter = toMovementConverter;
    }

    private Store fetchStore(String  id){
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

    public StoreDTO getStore(String id)  {
        Store store = fetchStore(id);
        return toStoreDTOConverter.convert(store);
    }

    public StoreDTO addStore(StoreDTO storeDTO) {
        Store toSave = toStoreConverter.convert(storeDTO);
        toSave.setStoreCode(RandomStringUtils.randomAlphabetic(5));
        Store saved = storeRepository.save(toSave);
        return toStoreDTOConverter.convert(saved);
    }

    public void deleteStore(String id)  {
        Store store = fetchStore(id);
        storeRepository.delete(store);
    }

    public StoreDTO updateStore(String id, StoreDTO storeDTO) {
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

    public InStoreProductDTO addProduct(String storeId, MovementDTO movementDTO){
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

        Optional<Product> optionalProduct = productRepository.findByProductCode(inStoreProduct.getProduct().getProductCode());
        if(!optionalProduct.isPresent())
            throw new ResourceNotFoundException("Product code: "+inStoreProduct.getProduct().getProductCode()+"non existing");

        boolean productExisting = store.getInStoreProducts()
                .stream()
                .anyMatch(inStoreProduct1 ->
                        inStoreProduct1.getProduct().getProductCode().equals(inStoreProduct.getProduct().getProductCode()));

        if(productExisting) {
            throw new ResourceNotValidException("Product already exists in store");
        }

        Product product = optionalProduct.get();
        product.addInStoreProduct(inStoreProduct);
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
                .tagCode(RandomStringUtils.randomAlphabetic(10))
                .type("Tag type2")
                .name("Tag2 ")
                .build();
        tag.setProduct(inStoreProduct);
        inStoreProduct.setTag(tag);

        store.addInStoreProduct(inStoreProduct);

        productRepository.save(product);

        storeRepository.save(store);

        return toInStoreProductDTOConverter.convert(inStoreProduct);
    }

    public void removeProduct(String storeId, MovementDTO movementDTO) {
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
    }

    public StoreDTO fetchInStoreProducts(String id) {
        Store store = fetchStore(id);
        Set<InStoreProductDTO> productDTOS = store.getInStoreProducts()
                .stream()
                .map(toInStoreProductDTOConverter::convert)
                .collect(Collectors.toSet());
        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setInStoreProducts(productDTOS);
        return dto;
    }

    public StoreDTO fetchCategoryInStoreProducts(String id, String categoryId) {
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

    public Set<InStoreProductDTO> getBelowThreshold(String id){
        Store store = fetchStore(id);
        return store.getInStoreProducts()
                .stream().filter(InStoreProduct::isAlertThreshold)
                .map(toInStoreProductDTOConverter::convert)
                .collect(Collectors.toSet());
    }

    public InStoreProductDTO fetchInStoreProduct(String storeId, String productId){
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

