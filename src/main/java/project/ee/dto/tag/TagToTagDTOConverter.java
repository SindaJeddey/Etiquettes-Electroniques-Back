package project.ee.dto.tag;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.dto.inStoreProduct.InStoreProductToInStoreProductDTOConverter;
import project.ee.models.models.Tag;

@Component
public class TagToTagDTOConverter implements Converter<Tag,TagDTO> {

    private final InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter;

    public TagToTagDTOConverter(InStoreProductToInStoreProductDTOConverter toInStoreProductDTOConverter) {
        this.toInStoreProductDTOConverter = toInStoreProductDTOConverter;
    }

    @Override
    public TagDTO convert(Tag tag) {
        if(tag == null)
            return null;
        return TagDTO.builder()
                .id(tag.getId())
                .code(tag.getCode())
                .name(tag.getName())
                .type(tag.getType())
                .product(toInStoreProductDTOConverter.convert(tag.getProduct()))
                .transmitterId(tag.getTransmitter().getId())
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
