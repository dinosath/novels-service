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
 * A Genre.
 */
@Entity
@Table(name = "genre")
@Cacheable
@RegisterForReflection
public class Genre extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    public String name;

    @ManyToMany(mappedBy = "genres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<Novel> novels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genre)) {
            return false;
        }
        return id != null && id.equals(((Genre) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Genre{" + "id=" + id + ", name='" + name + "'" + "}";
    }

    public Genre update() {
        return update(this);
    }

    public Genre persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Genre update(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("genre can't be null");
        }
        var entity = Genre.<Genre>findById(genre.id);
        if (entity != null) {
            entity.name = genre.name;
            entity.novels = genre.novels;
        }
        return entity;
    }

    public static Genre persistOrUpdate(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("genre can't be null");
        }
        if (genre.id == null) {
            persist(genre);
            return genre;
        } else {
            return update(genre);
        }
    }

    public static PanacheQuery<Genre> findAllWithEagerRelationships() {
        return find("select distinct genre from Genre genre");
    }

    public static Optional<Genre> findOneWithEagerRelationships(Long id) {
        return find("select genre from Genre genre where genre.id =?1", id).firstResultOptional();
    }
}
