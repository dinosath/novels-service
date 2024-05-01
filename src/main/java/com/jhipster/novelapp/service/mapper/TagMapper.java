package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.*;
import com.jhipster.novelapp.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "novels", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    default Tag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.id = id;
        return tag;
    }
}
