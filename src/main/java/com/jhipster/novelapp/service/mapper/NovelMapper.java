package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.Author;
import com.jhipster.novelapp.domain.Genre;
import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.domain.Tag;
import com.jhipster.novelapp.service.dto.AuthorDTO;
import com.jhipster.novelapp.service.dto.GenreDTO;
import com.jhipster.novelapp.service.dto.NovelDTO;
import com.jhipster.novelapp.service.dto.TagDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Novel} and its DTO {@link NovelDTO}.
 */
@Mapper(componentModel = "spring")
public interface NovelMapper extends EntityMapper<NovelDTO, Novel> {
    @Mapping(target = "genres", source = "genres", qualifiedByName = "genreNameSet")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagNameSet")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "authorNameSet")
    NovelDTO toDto(Novel s);

    @Mapping(target = "removeGenre", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    @Mapping(target = "removeAuthor", ignore = true)
    Novel toEntity(NovelDTO novelDTO);

    @Named("genreName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GenreDTO toDtoGenreName(Genre genre);

    @Named("genreNameSet")
    default Set<GenreDTO> toDtoGenreNameSet(Set<Genre> genre) {
        return genre.stream().map(this::toDtoGenreName).collect(Collectors.toSet());
    }

    @Named("tagName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TagDTO toDtoTagName(Tag tag);

    @Named("tagNameSet")
    default Set<TagDTO> toDtoTagNameSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagName).collect(Collectors.toSet());
    }

    @Named("authorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AuthorDTO toDtoAuthorName(Author author);

    @Named("authorNameSet")
    default Set<AuthorDTO> toDtoAuthorNameSet(Set<Author> author) {
        return author.stream().map(this::toDtoAuthorName).collect(Collectors.toSet());
    }
}
