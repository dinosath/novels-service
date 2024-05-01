package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.*;
import com.jhipster.novelapp.service.dto.ChapterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chapter} and its DTO {@link ChapterDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { NovelMapper.class })
public interface ChapterMapper extends EntityMapper<ChapterDTO, Chapter> {
    @Mapping(source = "novel.id", target = "novelId")
    @Mapping(source = "novel.title", target = "novelTitle")
    ChapterDTO toDto(Chapter chapter);

    @Mapping(source = "novelId", target = "novel")
    Chapter toEntity(ChapterDTO chapterDTO);

    default Chapter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chapter chapter = new Chapter();
        chapter.id = id;
        return chapter;
    }
}
