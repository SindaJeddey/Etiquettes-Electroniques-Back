package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.store.StoreDTO;
import project.ee.services.StoreService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(StoreController.BASE_URI)
@PreAuthorize("permitAll()")
public class StoreController {

    public static final String BASE_URI="/api/stores";

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public List<StoreDTO> getAllStores(){
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    public StoreDTO getStore(@PathVariable String id){
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

    @PostMapping()
    public StoreDTO newStore(@RequestBody StoreDTO storeDTO){
        return storeService.addStore(storeDTO);
    }
    
    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable String id){
        storeService.deleteStore(id);
    }

    @PutMapping("/{id}")
    public StoreDTO updateStore(@PathVariable String id, @RequestBody StoreDTO storeDTO){
        return storeService.updateStore(id,storeDTO);
    }

    @GetMapping("/{id}/products")
    public StoreDTO fetchStoreAllProducts(@PathVariable String id){
        return storeService.fetchInStoreProducts(id);
    }

    @GetMapping("/{id}/products/category/{categoryId}")
    public StoreDTO fetchStoreAllProductsPerCategory(@PathVariable String id, @PathVariable String categoryId){
        return storeService.fetchCategoryInStoreProducts(id,categoryId);
    }

    @PutMapping("/{id}/products/add")
    public InStoreProductDTO addProduct(@PathVariable String id, @RequestBody MovementDTO movementDTO){
        return storeService.addProduct(id,movementDTO);
    }

    @PutMapping("/{id}/products/delete")
    public void deleteInStoreProduct(@PathVariable String id, @RequestBody MovementDTO movementDTO){
        storeService.removeProduct(id,movementDTO);
    }

    @GetMapping("/{id}/threshold")
    public Set<InStoreProductDTO> getBelowThresholdProducts (@PathVariable String id){
        return storeService.getBelowThreshold(id);
    }


    @GetMapping("/{id}/products/{productId}")
    public InStoreProductDTO fetchInStoreProduct(@PathVariable String id,@PathVariable String productId){
        return storeService.fetchInStoreProduct(id,productId);
    }

}
