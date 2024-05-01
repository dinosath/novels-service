package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.*;
import com.jhipster.novelapp.service.dto.NovelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Novel} and its DTO {@link NovelDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { GenreMapper.class, TagMapper.class, AuthorMapper.class })
public interface NovelMapper extends EntityMapper<NovelDTO, Novel> {
    @Mapping(target = "chapters", ignore = true)
    Novel toEntity(NovelDTO novelDTO);

    default Novel fromId(Long id) {
        if (id == null) {
            return null;
        }
        Novel novel = new Novel();
        novel.id = id;
        return novel;
    }
}
