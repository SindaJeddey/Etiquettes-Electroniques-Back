package project.ee.dto.tag;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.SneakyThrows;
import project.ee.dto.inStoreProduct.InStoreProductDTOToInStoreProductConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Tag;
import project.ee.repositories.TransmitterRepository;

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
                .id(tagDTO.getId())
                .type(tagDTO.getType())
                .code(tagDTO.getCode())
                .name(tagDTO.getName())
                .product(toInStoreProductConverter.convert(tagDTO.getProduct()))
                .transmitter(transmitterRepository.findById(tagDTO.getId())
                        .orElseThrow(() ->
                                new NotFoundException("Transmitter id "+tagDTO.getTransmitterId()+"  not found")))
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
