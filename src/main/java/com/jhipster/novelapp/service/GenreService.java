package com.jhipster.novelapp.service;

import com.jhipster.novelapp.domain.Genre;
import com.jhipster.novelapp.service.dto.GenreDTO;
import com.jhipster.novelapp.service.mapper.GenreMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class GenreService {

    private final Logger log = LoggerFactory.getLogger(GenreService.class);

    @Inject
    GenreMapper genreMapper;

    @Transactional
    public GenreDTO persistOrUpdate(GenreDTO genreDTO) {
        log.debug("Request to save Genre : {}", genreDTO);
        var genre = genreMapper.toEntity(genreDTO);
        genre = Genre.persistOrUpdate(genre);
        return genreMapper.toDto(genre);
    }

    /**
     * Delete the Genre by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        Genre
            .findByIdOptional(id)
            .ifPresent(genre -> {
                genre.delete();
            });
    }

    /**
     * Get one genre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<GenreDTO> findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        return Genre.findOneWithEagerRelationships(id).map(genre -> genreMapper.toDto((Genre) genre));
    }

    /**
     * Get all the genres.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<GenreDTO> findAll(Page page) {
        log.debug("Request to get all Genres");
        return new Paged<>(Genre.findAll().page(page)).map(genre -> genreMapper.toDto((Genre) genre));
    }

    /**
     * Get all the genres with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<GenreDTO> findAllWithEagerRelationships(Page page) {
        var genres = Genre.findAllWithEagerRelationships().page(page).list();
        var totalCount = Genre.findAll().count();
        var pageCount = Genre.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, genres).map(genre -> genreMapper.toDto((Genre) genre));
    }
}
