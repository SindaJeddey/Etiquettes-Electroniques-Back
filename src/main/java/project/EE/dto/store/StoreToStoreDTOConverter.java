package project.EE.dto.store;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.EE.models.models.Store;


@Component
public class StoreToStoreDTOConverter implements Converter<Store,StoreDTO> {
    @Override
    public StoreDTO convert(Store store) {
        if(store == null)
            return null;
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(store.getId());
        storeDTO.setName(store.getName());
        storeDTO.setLocation(store.getLocation());
        storeDTO.setZipCode(store.getZipCode());
        return storeDTO;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
