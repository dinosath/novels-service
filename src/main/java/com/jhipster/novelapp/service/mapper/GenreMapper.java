package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.Genre;
import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.service.dto.GenreDTO;
import com.jhipster.novelapp.service.dto.NovelDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Genre} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {
    @Mapping(target = "novels", source = "novels", qualifiedByName = "novelIdSet")
    GenreDTO toDto(Genre s);

    @Mapping(target = "novels", ignore = true)
    @Mapping(target = "removeNovel", ignore = true)
    Genre toEntity(GenreDTO genreDTO);

    @Named("novelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NovelDTO toDtoNovelId(Novel novel);

    @Named("novelIdSet")
    default Set<NovelDTO> toDtoNovelIdSet(Set<Novel> novel) {
        return novel.stream().map(this::toDtoNovelId).collect(Collectors.toSet());
    }
}
