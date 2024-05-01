package com.jhipster.novelapp.service;

import com.jhipster.novelapp.domain.Tag;
import com.jhipster.novelapp.service.dto.TagDTO;
import com.jhipster.novelapp.service.mapper.TagMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    @Inject
    TagMapper tagMapper;

    @Transactional
    public TagDTO persistOrUpdate(TagDTO tagDTO) {
        log.debug("Request to save Tag : {}", tagDTO);
        var tag = tagMapper.toEntity(tagDTO);
        tag = Tag.persistOrUpdate(tag);
        return tagMapper.toDto(tag);
    }

    /**
     * Delete the Tag by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        Tag
            .findByIdOptional(id)
            .ifPresent(tag -> {
                tag.delete();
            });
    }

    /**
     * Get one tag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TagDTO> findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        return Tag.findOneWithEagerRelationships(id).map(tag -> tagMapper.toDto((Tag) tag));
    }

    /**
     * Get all the tags.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<TagDTO> findAll(Page page) {
        log.debug("Request to get all Tags");
        return new Paged<>(Tag.findAll().page(page)).map(tag -> tagMapper.toDto((Tag) tag));
    }

    /**
     * Get all the tags with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<TagDTO> findAllWithEagerRelationships(Page page) {
        var tags = Tag.findAllWithEagerRelationships().page(page).list();
        var totalCount = Tag.findAll().count();
        var pageCount = Tag.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, tags).map(tag -> tagMapper.toDto((Tag) tag));
    }
}
