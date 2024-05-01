package com.jhipster.novelapp.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.jhipster.novelapp.domain.Author} entity.
 */
@RegisterForReflection
public class AuthorDTO implements Serializable {

    public Long id;

    @NotNull
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorDTO)) {
            return false;
        }

        return id != null && id.equals(((AuthorDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AuthorDTO{" + ", id=" + id + ", name='" + name + "'" + "}";
    }
}
