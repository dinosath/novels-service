package com.jhipster.novelapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Novel.
 */
@Entity
@Table(name = "novel")
@Cacheable
@RegisterForReflection
public class Novel extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    public String title;

    @OneToMany(mappedBy = "novel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Chapter> chapters = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_novel__genre",
        joinColumns = @JoinColumn(name = "novel_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_novel__tag",
        joinColumns = @JoinColumn(name = "novel_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_novel__author",
        joinColumns = @JoinColumn(name = "novel_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Author> authors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Novel)) {
            return false;
        }
        return id != null && id.equals(((Novel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Novel{" + "id=" + id + ", title='" + title + "'" + "}";
    }

    public Novel update() {
        return update(this);
    }

    public Novel persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Novel update(Novel novel) {
        if (novel == null) {
            throw new IllegalArgumentException("novel can't be null");
        }
        var entity = Novel.<Novel>findById(novel.id);
        if (entity != null) {
            entity.title = novel.title;
            entity.chapters = novel.chapters;
            entity.genres = novel.genres;
            entity.tags = novel.tags;
            entity.authors = novel.authors;
        }
        return entity;
    }

    public static Novel persistOrUpdate(Novel novel) {
        if (novel == null) {
            throw new IllegalArgumentException("novel can't be null");
        }
        if (novel.id == null) {
            persist(novel);
            return novel;
        } else {
            return update(novel);
        }
    }

    public static PanacheQuery<Novel> findAllWithEagerRelationships() {
        return find(
            "select distinct novel from Novel novel left join fetch novel.genres left join fetch novel.tags left join fetch novel.authors"
        );
    }

    public static Optional<Novel> findOneWithEagerRelationships(Long id) {
        return find(
            "select novel from Novel novel left join fetch novel.genres left join fetch novel.tags left join fetch novel.authors where novel.id =?1",
            id
        )
            .firstResultOptional();
    }
}
