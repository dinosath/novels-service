package com.jhipster.novelapp.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.jhipster.novelapp.domain.Genre} entity.
 */
@RegisterForReflection
public class GenreDTO implements Serializable {

    public Long id;

    @NotNull
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenreDTO)) {
            return false;
        }

        return id != null && id.equals(((GenreDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GenreDTO{" + ", id=" + id + ", name='" + name + "'" + "}";
    }
}
