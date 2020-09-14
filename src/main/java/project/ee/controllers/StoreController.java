package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.store.StoreDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Product;
import project.ee.services.StoreService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/stores")
@PreAuthorize("permitAll()")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public List<StoreDTO> getAllStores(){
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    public StoreDTO getStore(@PathVariable String id) throws NotFoundException {
        return storeService.getStore(id);
    }

    @GetMapping("/locations")
    public Set<String> getAllLocations(){
        return storeService.getAllLocations();
    }

    @GetMapping("/locations/{location}")
    public List<StoreDTO> getStoresByLocation(@PathVariable String location){
        return storeService.getAllStoresByLocation(location);
    }

    @PostMapping("/new")
    public StoreDTO newStore(@RequestBody StoreDTO storeDTO){
        if (storeDTO == null)
            throw new IllegalArgumentException("Must provide a store to save");
        return storeService.addStore(storeDTO);
    }
    
    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable String id) throws NotFoundException {
        storeService.deleteStore(id);
    }

    @PutMapping("/{id}")
    public StoreDTO updateStore(@PathVariable String id, @RequestBody StoreDTO storeDTO)
            throws NotFoundException {
        return storeService.updateStore(id,storeDTO);
    }

    @GetMapping("/{id}/products")
    public StoreDTO fetchStoreAllProducts(@PathVariable String id) throws NotFoundException {
        return storeService.fetchInStoreProducts(id);
    }

    @GetMapping("/{id}/products/category/{categoryId}")
    public StoreDTO fetchStoreAllProductsPerCategory(@PathVariable String id, @PathVariable Long categoryId)
            throws NotFoundException {
        return storeService.fetchCategoryInStoreProducts(id,categoryId);
    }

    @PutMapping("/{id}/products/{operation}")
    public Set<InStoreProductDTO> addProduct(@PathVariable String id,
                                             @PathVariable String operation,
                                             @RequestBody MovementDTO movementDTO)
            throws NotFoundException {
        switch (operation){
            case "add":
                return storeService.addProduct(id,movementDTO);
            case "delete":
                return storeService.removeProduct(id,movementDTO);
            default:
                throw new RuntimeException("Invalid operation");
        }
    }

    @GetMapping("/{id}/threshold")
    public Set<ProductDTO> getBelowThresholdProducts (@PathVariable String id) throws NotFoundException {
        return storeService.getBelowThreshold(id);
    }

        @GetMapping("/{id}/products/{productId}")
    public InStoreProductDTO fetchInStoreProduct(@PathVariable String id,@PathVariable String productId) throws NotFoundException {
        return storeService.fetchInStoreProduct(id,productId);
    }

}
