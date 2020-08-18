package project.EE.dto.store;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.EE.models.models.Store;

@Component
public class StoreDTOToStoreConverter implements Converter<StoreDTO, Store> {
    @Override
    public Store convert(StoreDTO storeDTO) {
        if (storeDTO == null)
            return null;
        Store store = Store.builder()
                .id(storeDTO.getId())
                .name(storeDTO.getName())
                .location(storeDTO.getLocation())
                .zipCode(storeDTO.getZipCode())
                .build();
        return store;
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
