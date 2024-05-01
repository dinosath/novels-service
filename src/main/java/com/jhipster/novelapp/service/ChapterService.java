package com.jhipster.novelapp.service;

import com.jhipster.novelapp.domain.Chapter;
import com.jhipster.novelapp.service.dto.ChapterDTO;
import com.jhipster.novelapp.service.mapper.ChapterMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class ChapterService {

    private final Logger log = LoggerFactory.getLogger(ChapterService.class);

    @Inject
    ChapterMapper chapterMapper;

    @Transactional
    public ChapterDTO persistOrUpdate(ChapterDTO chapterDTO) {
        log.debug("Request to save Chapter : {}", chapterDTO);
        var chapter = chapterMapper.toEntity(chapterDTO);
        chapter = Chapter.persistOrUpdate(chapter);
        return chapterMapper.toDto(chapter);
    }

    /**
     * Delete the Chapter by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Chapter : {}", id);
        Chapter
            .findByIdOptional(id)
            .ifPresent(chapter -> {
                chapter.delete();
            });
    }

    /**
     * Get one chapter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ChapterDTO> findOne(Long id) {
        log.debug("Request to get Chapter : {}", id);
        return Chapter.findByIdOptional(id).map(chapter -> chapterMapper.toDto((Chapter) chapter));
    }

    /**
     * Get all the chapters.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ChapterDTO> findAll(Page page) {
        log.debug("Request to get all Chapters");
        return new Paged<>(Chapter.findAll().page(page)).map(chapter -> chapterMapper.toDto((Chapter) chapter));
    }
}
