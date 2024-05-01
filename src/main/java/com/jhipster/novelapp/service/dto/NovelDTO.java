package com.jhipster.novelapp.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.jhipster.novelapp.domain.Novel} entity.
 */
@RegisterForReflection
public class NovelDTO implements Serializable {

    public Long id;

    @NotNull
    public String title;

    public Set<GenreDTO> genres = new HashSet<>();
    public Set<TagDTO> tags = new HashSet<>();
    public Set<AuthorDTO> authors = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NovelDTO)) {
            return false;
        }

        return id != null && id.equals(((NovelDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "NovelDTO{" +
            ", id=" +
            id +
            ", title='" +
            title +
            "'" +
            ", genres='" +
            genres +
            "'" +
            ", tags='" +
            tags +
            "'" +
            ", authors='" +
            authors +
            "'" +
            "}"
        );
    }
}
