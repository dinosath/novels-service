package com.jhipster.novelapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.jhipster.novelapp.domain.Genre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GenreDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<NovelDTO> novels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<NovelDTO> getNovels() {
        return novels;
    }

    public void setNovels(Set<NovelDTO> novels) {
        this.novels = novels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenreDTO)) {
            return false;
        }

        GenreDTO genreDTO = (GenreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenreDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", novels=" + getNovels() +
            "}";
    }
}
