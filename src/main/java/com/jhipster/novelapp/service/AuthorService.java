package com.jhipster.novelapp.service;

import com.jhipster.novelapp.domain.Author;
import com.jhipster.novelapp.service.dto.AuthorDTO;
import com.jhipster.novelapp.service.mapper.AuthorMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorService.class);

    @Inject
    AuthorMapper authorMapper;

    @Transactional
    public AuthorDTO persistOrUpdate(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        var author = authorMapper.toEntity(authorDTO);
        author = Author.persistOrUpdate(author);
        return authorMapper.toDto(author);
    }

    /**
     * Delete the Author by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        Author
            .findByIdOptional(id)
            .ifPresent(author -> {
                author.delete();
            });
    }

    /**
     * Get one author by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AuthorDTO> findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        return Author.findOneWithEagerRelationships(id).map(author -> authorMapper.toDto((Author) author));
    }

    /**
     * Get all the authors.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AuthorDTO> findAll(Page page) {
        log.debug("Request to get all Authors");
        return new Paged<>(Author.findAll().page(page)).map(author -> authorMapper.toDto((Author) author));
    }

    /**
     * Get all the authors with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AuthorDTO> findAllWithEagerRelationships(Page page) {
        var authors = Author.findAllWithEagerRelationships().page(page).list();
        var totalCount = Author.findAll().count();
        var pageCount = Author.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, authors).map(author -> authorMapper.toDto((Author) author));
    }
}
