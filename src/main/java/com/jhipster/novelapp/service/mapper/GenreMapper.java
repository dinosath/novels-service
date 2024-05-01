package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.*;
import com.jhipster.novelapp.service.dto.GenreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Genre} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {
    @Mapping(target = "novels", ignore = true)
    Genre toEntity(GenreDTO genreDTO);

    default Genre fromId(Long id) {
        if (id == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.id = id;
        return genre;
    }
}
