package project.ee.services;

import org.springframework.stereotype.Service;
import project.ee.dto.category.CategoryDTOToCategoryConverter;
import project.ee.dto.category.CategoryToCategoryDTOConverter;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.inStoreProduct.InStoreProductDTOToInStoreProductConverter;
import project.ee.dto.inStoreProduct.InStoreProductToInStoreProductDTOConverter;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.movement.MovementDTOToMovementConverter;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.dto.store.StoreDTO;
import project.ee.dto.store.StoreDTOToStoreConverter;
import project.ee.dto.store.StoreToStoreDTOConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.*;
import project.ee.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StoreService {


    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InStoreProductRepository inStoreProductRepository;
    private final MovementRepository movementRepository;
    private final StoreDTOToStoreConverter toStoreConverter;
    private final StoreToStoreDTOConverter toStoreDTOConverter;
    private final InStoreProductDTOToInStoreProductConverter toInStoreProductConverter;
    private final InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter;
    private final MovementDTOToMovementConverter toMovementConverter;

    public StoreService(StoreRepository storeRepository,
                        ProductRepository productRepository,
                        InStoreProductRepository inStoreProductRepository,
                        MovementRepository movementRepository,
                        StoreDTOToStoreConverter toStoreConverter,
                        StoreToStoreDTOConverter toStoreDTOConverter,
                        InStoreProductDTOToInStoreProductConverter toInStoreProductConverter,
                        InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter,
                        MovementDTOToMovementConverter toMovementConverter) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inStoreProductRepository = inStoreProductRepository;
        this.movementRepository = movementRepository;
        this.toStoreConverter = toStoreConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
        this.toInStoreProductConverter = toInStoreProductConverter;
        this.toInStoreProductDTOConverter = toInStoreProductDTOConverter;
        this.toMovementConverter = toMovementConverter;
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
                .map(toStoreDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public StoreDTO getStore(Long id) throws NotFoundException {
        Store store = fetchStore(id);
        return toStoreDTOConverter.convert(store);
    }

    public StoreDTO addStore(StoreDTO storeDTO) {
        Store toSave = toStoreConverter.convert(storeDTO);
        Store saved = storeRepository.save(toSave);
        return toStoreDTOConverter.convert(saved);
    }

    public void deleteStore(Long id) throws NotFoundException {
        Store store = fetchStore(id);
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
        Store saved = storeRepository.save(store);
        return toStoreDTOConverter.convert(saved);
    }

    public void addProduct(Long storeId, MovementDTO movementDTO) throws NotFoundException {
        Store store = fetchStore(storeId);

        InStoreProduct inStoreProduct = toInStoreProductConverter.convert(movementDTO.getProduct());
        if(inStoreProduct.getProduct() == null)
            throw new RuntimeException("Must provide product to add to store");
        Optional<Product> product = productRepository.findById(inStoreProduct.getProduct().getId());
        if(!product.isPresent())
            throw new NotFoundException("Product non existing");
        inStoreProduct.setStore(store);
        inStoreProduct.setProduct(product.get());

        Movement movement = toMovementConverter.convert(movementDTO);
        movement.setMovementDate(LocalDate.now());
        movement.setProduct(inStoreProduct);
        Movement savedMovement = movementRepository.save(movement);
        inStoreProduct.addMovement(savedMovement);

        store.addInStoreProduct(inStoreProduct);
        storeRepository.save(store);
    }

    public void removeProduct(Long id, Long productId) throws NotFoundException {
        Store store = fetchStore(id);

        InStoreProduct product = inStoreProductRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product id: "+productId+" not found in store: "+id));

        store.removeInStoreProduct(product);
        storeRepository.save(store);
    }

    public StoreDTO fetchInStoreProducts(Long id) throws NotFoundException {
        Store store = fetchStore(id);
        Set<InStoreProductDTO> productDTOS = store.getInStoreProducts()
                .stream()
                .map(toInStoreProductDTOConverter::convert)
                .collect(Collectors.toSet());
        StoreDTO dto = toStoreDTOConverter.convert(store);
        dto.setInStoreProducts(productDTOS);
        return dto;
    }
}
