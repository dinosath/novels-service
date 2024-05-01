package com.jhipster.novelapp.service.mapper;

import com.jhipster.novelapp.domain.*;
import com.jhipster.novelapp.service.dto.AuthorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Mapping(target = "novels", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    default Author fromId(Long id) {
        if (id == null) {
            return null;
        }
        Author author = new Author();
        author.id = id;
        return author;
    }
}
