package project.ee.dto.tag;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import project.ee.dto.inStoreProduct.InStoreProductDTOToInStoreProductConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Tag;
import project.ee.repositories.TransmitterRepository;

@Component
public class TagDTOToTagConverter implements Converter<TagDTO, Tag> {

    private final InStoreProductDTOToInStoreProductConverter toInStoreProductConverter;
    private final TransmitterRepository transmitterRepository;

    public TagDTOToTagConverter(InStoreProductDTOToInStoreProductConverter toInStoreProductConverter,
                                TransmitterRepository transmitterRepository) {
        this.toInStoreProductConverter = toInStoreProductConverter;
        this.transmitterRepository = transmitterRepository;
    }

    @SneakyThrows
    @Override
    public Tag convert(TagDTO tagDTO) {
        if (tagDTO == null)
            return null;
        return Tag.builder()
                .type(tagDTO.getType())
                .tagCode(tagDTO.getCode())
                .name(tagDTO.getName())
                .product(toInStoreProductConverter.convert(tagDTO.getProduct()))
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
