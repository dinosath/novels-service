package com.jhipster.novelapp.service;

import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.repository.NovelRepository;
import com.jhipster.novelapp.service.dto.NovelDTO;
import com.jhipster.novelapp.service.mapper.NovelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.jhipster.novelapp.domain.Novel}.
 */
@Service
@Transactional
public class NovelService {

    private final Logger log = LoggerFactory.getLogger(NovelService.class);

    private final NovelRepository novelRepository;

    private final NovelMapper novelMapper;

    public NovelService(NovelRepository novelRepository, NovelMapper novelMapper) {
        this.novelRepository = novelRepository;
        this.novelMapper = novelMapper;
    }

    /**
     * Save a novel.
     *
     * @param novelDTO the entity to save.
     * @return the persisted entity.
     */
    public NovelDTO save(NovelDTO novelDTO) {
        log.debug("Request to save Novel : {}", novelDTO);
        Novel novel = novelMapper.toEntity(novelDTO);
        novel = novelRepository.save(novel);
        return novelMapper.toDto(novel);
    }

    /**
     * Update a novel.
     *
     * @param novelDTO the entity to save.
     * @return the persisted entity.
     */
    public NovelDTO update(NovelDTO novelDTO) {
        log.debug("Request to update Novel : {}", novelDTO);
        Novel novel = novelMapper.toEntity(novelDTO);
        novel = novelRepository.save(novel);
        return novelMapper.toDto(novel);
    }

    /**
     * Partially update a novel.
     *
     * @param novelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NovelDTO> partialUpdate(NovelDTO novelDTO) {
        log.debug("Request to partially update Novel : {}", novelDTO);

        return novelRepository
            .findById(novelDTO.getId())
            .map(existingNovel -> {
                novelMapper.partialUpdate(existingNovel, novelDTO);

                return existingNovel;
            })
            .map(novelRepository::save)
            .map(novelMapper::toDto);
    }

    /**
     * Get all the novels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NovelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Novels");
        return novelRepository.findAll(pageable).map(novelMapper::toDto);
    }

    /**
     * Get all the novels with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NovelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return novelRepository.findAllWithEagerRelationships(pageable).map(novelMapper::toDto);
    }

    /**
     * Get one novel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NovelDTO> findOne(Long id) {
        log.debug("Request to get Novel : {}", id);
        return novelRepository.findOneWithEagerRelationships(id).map(novelMapper::toDto);
    }

    /**
     * Delete the novel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Novel : {}", id);
        novelRepository.deleteById(id);
    }
}
