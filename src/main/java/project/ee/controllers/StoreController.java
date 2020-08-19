package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.store.StoreDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.services.StoreService;

import java.util.List;

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

    @PutMapping("/{id}/{choice}/{choiceId}/{operation}")
    public StoreDTO updateStore(@PathVariable Long id,
                                @PathVariable String choice,
                                @PathVariable Long choiceId,
                                @PathVariable String operation)
            throws NotFoundException {
        switch (choice){
            case "categories": {
                if(operation.equals("add"))
                    return storeService.addCategory(id,choiceId);
                else if(operation.equals("delete"))
                    return storeService.removeCategory(id,choiceId);
                else
                    throw new RuntimeException("Operation invalid");
            }

            case "products": {
                if(operation.equals("add"))
                    return storeService.addProduct(id,choiceId);
                else if(operation.equals("delete"))
                    return storeService.removeProduct(id,choiceId);
                else
                    throw new RuntimeException("Operation invalid");
            }

            default:
                throw new RuntimeException("Choice invalid");
        }
    }

    @GetMapping("/{id}/{choice}")
    public StoreDTO fetch(@PathVariable Long id,
                          @PathVariable String choice) throws NotFoundException {

        switch (choice){
            case "categories":{
                return storeService.fetchCategories(id);
            }
            case "products":{
                return storeService.fetchProducts(id);
            }
            default:
                throw new RuntimeException("Choice invalid");
        }
    }

}
