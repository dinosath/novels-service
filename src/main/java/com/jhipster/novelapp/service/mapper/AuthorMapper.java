package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.Author;
import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.service.dto.AuthorDTO;
import com.jhipster.novelapp.service.dto.NovelDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Mapping(target = "novels", source = "novels", qualifiedByName = "novelIdSet")
    AuthorDTO toDto(Author s);

    @Mapping(target = "novels", ignore = true)
    @Mapping(target = "removeNovel", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    @Named("novelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NovelDTO toDtoNovelId(Novel novel);

    @Named("novelIdSet")
    default Set<NovelDTO> toDtoNovelIdSet(Set<Novel> novel) {
        return novel.stream().map(this::toDtoNovelId).collect(Collectors.toSet());
    }
}
