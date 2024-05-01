package com.jhipster.novelapp.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.jhipster.novelapp.domain.Chapter} entity.
 */
@RegisterForReflection
public class ChapterDTO implements Serializable {

    public Long id;

    @NotNull
    public String title;

    @Lob
    public String content;

    public Long novelId;
    public String novelTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChapterDTO)) {
            return false;
        }

        return id != null && id.equals(((ChapterDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ChapterDTO{" +
            ", id=" +
            id +
            ", title='" +
            title +
            "'" +
            ", content='" +
            content +
            "'" +
            ", novelId=" +
            novelId +
            ", novelTitle='" +
            novelTitle +
            "'" +
            "}"
        );
    }
}
