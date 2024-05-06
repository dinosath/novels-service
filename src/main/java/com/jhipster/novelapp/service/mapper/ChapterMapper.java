package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.Chapter;
import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.service.dto.ChapterDTO;
import com.jhipster.novelapp.service.dto.NovelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chapter} and its DTO {@link ChapterDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChapterMapper extends EntityMapper<ChapterDTO, Chapter> {
    @Mapping(target = "novel", source = "novel", qualifiedByName = "novelTitle")
    ChapterDTO toDto(Chapter s);

    @Named("novelTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    NovelDTO toDtoNovelTitle(Novel novel);
}
