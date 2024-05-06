package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.domain.Tag;
import com.jhipster.novelapp.service.dto.NovelDTO;
import com.jhipster.novelapp.service.dto.TagDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "novels", source = "novels", qualifiedByName = "novelIdSet")
    TagDTO toDto(Tag s);

    @Mapping(target = "novels", ignore = true)
    @Mapping(target = "removeNovel", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    @Named("novelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NovelDTO toDtoNovelId(Novel novel);

    @Named("novelIdSet")
    default Set<NovelDTO> toDtoNovelIdSet(Set<Novel> novel) {
        return novel.stream().map(this::toDtoNovelId).collect(Collectors.toSet());
    }
}
