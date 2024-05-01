package com.jhipster.novelapp.service;

import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.service.dto.NovelDTO;
import com.jhipster.novelapp.service.mapper.NovelMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class NovelService {

    private final Logger log = LoggerFactory.getLogger(NovelService.class);

    @Inject
    NovelMapper novelMapper;

    @Transactional
    public NovelDTO persistOrUpdate(NovelDTO novelDTO) {
        log.debug("Request to save Novel : {}", novelDTO);
        var novel = novelMapper.toEntity(novelDTO);
        novel = Novel.persistOrUpdate(novel);
        return novelMapper.toDto(novel);
    }

    /**
     * Delete the Novel by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Novel : {}", id);
        Novel
            .findByIdOptional(id)
            .ifPresent(novel -> {
                novel.delete();
            });
    }

    /**
     * Get one novel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<NovelDTO> findOne(Long id) {
        log.debug("Request to get Novel : {}", id);
        return Novel.findOneWithEagerRelationships(id).map(novel -> novelMapper.toDto((Novel) novel));
    }

    /**
     * Get all the novels.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<NovelDTO> findAll(Page page) {
        log.debug("Request to get all Novels");
        return new Paged<>(Novel.findAll().page(page)).map(novel -> novelMapper.toDto((Novel) novel));
    }

    /**
     * Get all the novels with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<NovelDTO> findAllWithEagerRelationships(Page page) {
        var novels = Novel.findAllWithEagerRelationships().page(page).list();
        var totalCount = Novel.findAll().count();
        var pageCount = Novel.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, novels).map(novel -> novelMapper.toDto((Novel) novel));
    }
}
