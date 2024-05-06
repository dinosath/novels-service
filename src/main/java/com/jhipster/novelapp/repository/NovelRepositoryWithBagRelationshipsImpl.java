package com.jhipster.novelapp.repository;

import com.jhipster.novelapp.domain.Novel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NovelRepositoryWithBagRelationshipsImpl implements NovelRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NOVELS_PARAMETER = "novels";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Novel> fetchBagRelationships(Optional<Novel> novel) {
        return novel.map(this::fetchGenres).map(this::fetchTags).map(this::fetchAuthors);
    }

    @Override
    public Page<Novel> fetchBagRelationships(Page<Novel> novels) {
        return new PageImpl<>(fetchBagRelationships(novels.getContent()), novels.getPageable(), novels.getTotalElements());
    }

    @Override
    public List<Novel> fetchBagRelationships(List<Novel> novels) {
        return Optional.of(novels).map(this::fetchGenres).map(this::fetchTags).map(this::fetchAuthors).orElse(Collections.emptyList());
    }

    Novel fetchGenres(Novel result) {
        return entityManager
            .createQuery("select novel from Novel novel left join fetch novel.genres where novel.id = :id", Novel.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Novel> fetchGenres(List<Novel> novels) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, novels.size()).forEach(index -> order.put(novels.get(index).getId(), index));
        List<Novel> result = entityManager
            .createQuery("select novel from Novel novel left join fetch novel.genres where novel in :novels", Novel.class)
            .setParameter(NOVELS_PARAMETER, novels)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Novel fetchTags(Novel result) {
        return entityManager
            .createQuery("select novel from Novel novel left join fetch novel.tags where novel.id = :id", Novel.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Novel> fetchTags(List<Novel> novels) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, novels.size()).forEach(index -> order.put(novels.get(index).getId(), index));
        List<Novel> result = entityManager
            .createQuery("select novel from Novel novel left join fetch novel.tags where novel in :novels", Novel.class)
            .setParameter(NOVELS_PARAMETER, novels)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Novel fetchAuthors(Novel result) {
        return entityManager
            .createQuery("select novel from Novel novel left join fetch novel.authors where novel.id = :id", Novel.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Novel> fetchAuthors(List<Novel> novels) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, novels.size()).forEach(index -> order.put(novels.get(index).getId(), index));
        List<Novel> result = entityManager
            .createQuery("select novel from Novel novel left join fetch novel.authors where novel in :novels", Novel.class)
            .setParameter(NOVELS_PARAMETER, novels)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
