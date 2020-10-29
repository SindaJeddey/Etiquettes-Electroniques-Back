package project.ee.dto.store;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Store;


@Component
public class StoreDTOToStoreConverter implements Converter<StoreDTO, Store> {

    @Override
    public Store convert(StoreDTO storeDTO) {
        if (storeDTO == null)
            return null;
        return Store.builder()
                .name(storeDTO.getName())
                .storeCode(storeDTO.getStoreCode())
                .location(storeDTO.getLocation())
                .zipCode(storeDTO.getZipCode())
                .build();
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
