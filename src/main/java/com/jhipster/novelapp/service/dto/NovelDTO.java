package com.jhipster.novelapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.jhipster.novelapp.domain.Novel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NovelDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Set<GenreDTO> genres = new HashSet<>();

    private Set<TagDTO> tags = new HashSet<>();

    private Set<AuthorDTO> authors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NovelDTO)) {
            return false;
        }

        NovelDTO novelDTO = (NovelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, novelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NovelDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", genres=" + getGenres() +
            ", tags=" + getTags() +
            ", authors=" + getAuthors() +
            "}";
    }
}
