package com.jhipster.novelapp.repository;

import com.jhipster.novelapp.domain.Novel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface NovelRepositoryWithBagRelationships {
    Optional<Novel> fetchBagRelationships(Optional<Novel> novel);

    List<Novel> fetchBagRelationships(List<Novel> novels);

    Page<Novel> fetchBagRelationships(Page<Novel> novels);
}
