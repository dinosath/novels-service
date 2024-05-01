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
 * A Author.
 */
@Entity
@Table(name = "author")
@Cacheable
@RegisterForReflection
public class Author extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    public String name;

    @ManyToMany(mappedBy = "authors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<Novel> novels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        return id != null && id.equals(((Author) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name='" + name + "'" + "}";
    }

    public Author update() {
        return update(this);
    }

    public Author persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Author update(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("author can't be null");
        }
        var entity = Author.<Author>findById(author.id);
        if (entity != null) {
            entity.name = author.name;
            entity.novels = author.novels;
        }
        return entity;
    }

    public static Author persistOrUpdate(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("author can't be null");
        }
        if (author.id == null) {
            persist(author);
            return author;
        } else {
            return update(author);
        }
    }

    public static PanacheQuery<Author> findAllWithEagerRelationships() {
        return find("select distinct author from Author author");
    }

    public static Optional<Author> findOneWithEagerRelationships(Long id) {
        return find("select author from Author author where author.id =?1", id).firstResultOptional();
    }
}
