package com.jhipster.novelapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Type;

/**
 * A Chapter.
 */
@Entity
@Table(name = "chapter")
@Cacheable
@RegisterForReflection
public class Chapter extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "content", nullable = false)
    public String content;

    @ManyToOne
    @JoinColumn(name = "novel_id")
    public Novel novel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chapter)) {
            return false;
        }
        return id != null && id.equals(((Chapter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Chapter{" + "id=" + id + ", title='" + title + "'" + ", content='" + content + "'" + "}";
    }

    public Chapter update() {
        return update(this);
    }

    public Chapter persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Chapter update(Chapter chapter) {
        if (chapter == null) {
            throw new IllegalArgumentException("chapter can't be null");
        }
        var entity = Chapter.<Chapter>findById(chapter.id);
        if (entity != null) {
            entity.title = chapter.title;
            entity.content = chapter.content;
            entity.novel = chapter.novel;
        }
        return entity;
    }

    public static Chapter persistOrUpdate(Chapter chapter) {
        if (chapter == null) {
            throw new IllegalArgumentException("chapter can't be null");
        }
        if (chapter.id == null) {
            persist(chapter);
            return chapter;
        } else {
            return update(chapter);
        }
    }
}
