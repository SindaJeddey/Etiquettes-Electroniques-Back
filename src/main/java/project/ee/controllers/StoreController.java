package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.store.StoreDTO;
import project.ee.exceptions.NotFoundException;
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
    public StoreDTO getStore(@PathVariable Long id) throws NotFoundException {
        return storeService.getStore(id);
    }

    @PostMapping("/new")
    public StoreDTO newStore(@RequestBody StoreDTO storeDTO){
        if (storeDTO == null)
            throw new IllegalArgumentException("Must provide a store to save");
        return storeService.addStore(storeDTO);
    }
    
    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable Long id) throws NotFoundException {
        storeService.deleteStore(id);
    }

    @PutMapping("/{id}")
    public StoreDTO updateStore(@PathVariable Long id, @RequestBody StoreDTO storeDTO)
            throws NotFoundException {
        return storeService.updateStore(id,storeDTO);
    }

    @GetMapping("/{id}/products")
    public StoreDTO fetchStoreAllProducts(@PathVariable Long id) throws NotFoundException {
        return storeService.fetchInStoreProducts(id);
    }

    @GetMapping("/{id}/products/category/{categoryId}")
    public StoreDTO fetchStoreAllProductsPerCategory(@PathVariable Long id, @PathVariable Long categoryId)
            throws NotFoundException {
        return storeService.fetchCategoryInStoreProducts(id,categoryId);
    }

    @PutMapping("/{id}/products/add")
    public Set<InStoreProductDTO> addProduct(@PathVariable Long id, @RequestBody MovementDTO movementDTO)
            throws NotFoundException {
        return storeService.addProduct(id,movementDTO);
    }

}
