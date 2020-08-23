package project.ee.dto.store;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.dto.category.CategoryDTOToCategoryConverter;
import project.ee.models.models.Store;

import java.util.stream.Collectors;

@Component
public class StoreDTOToStoreConverter implements Converter<StoreDTO, Store> {

    private final CategoryDTOToCategoryConverter toCategoryConverter;

    public StoreDTOToStoreConverter(CategoryDTOToCategoryConverter toCategoryConverter) {
        this.toCategoryConverter = toCategoryConverter;
    }

    @Override
    public Store convert(StoreDTO storeDTO) {
        if (storeDTO == null)
            return null;
        Store store = Store.builder()
                .id(storeDTO.getId())
                .name(storeDTO.getName())
                .location(storeDTO.getLocation())
                .zipCode(storeDTO.getZipCode())
                .categories(storeDTO.getCategories()
                        .stream().map(categoryDTO -> toCategoryConverter.convert(categoryDTO))
                        .collect(Collectors.toSet()))
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
