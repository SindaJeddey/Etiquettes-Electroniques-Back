package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.ee.dto.tag.TagDTO;
import project.ee.dto.tag.TagToTagDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.models.models.Tag;
import project.ee.repositories.TagRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final TagToTagDTOConverter toTagDTOConverter;

    public TagService(TagRepository tagRepository, TagToTagDTOConverter toTagDTOConverter) {
        this.tagRepository = tagRepository;
        this.toTagDTOConverter = toTagDTOConverter;
    }

    public TagDTO getTag(String id){
        Tag tag =  tagRepository.findByTagCode(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag id: "+id+" not found"));
        return toTagDTOConverter.convert(tag);
    }

    public Set<TagDTO> getStoreTags(String storeId) {
        return tagRepository.findAll()
                .stream().filter(tag -> tag.getProduct().getStore().getStoreCode().equals(storeId))
                .map(toTagDTOConverter::convert)
                .collect(Collectors.toSet());
    }

    public TagDTO getProductTag (String storeId, String productId) {
        Tag tag = tagRepository.findAll()
                .stream()
                .filter(tag1 ->
                        tag1.getProduct().getInStoreProductCode().equals(productId) &&
                                tag1.getProduct().getStore().getStoreCode().equals(storeId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Tag for product id: "+productId+" not found in store:"+storeId));
        return toTagDTOConverter.convert(tag);
    }

}
