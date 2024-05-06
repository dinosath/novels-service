package com.jhipster.novelapp.domain;

import static com.jhipster.novelapp.domain.GenreTestSamples.*;
import static com.jhipster.novelapp.domain.NovelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GenreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Genre.class);
        Genre genre1 = getGenreSample1();
        Genre genre2 = new Genre();
        assertThat(genre1).isNotEqualTo(genre2);

        genre2.setId(genre1.getId());
        assertThat(genre1).isEqualTo(genre2);

        genre2 = getGenreSample2();
        assertThat(genre1).isNotEqualTo(genre2);
    }

    @Test
    void novelTest() throws Exception {
        Genre genre = getGenreRandomSampleGenerator();
        Novel novelBack = getNovelRandomSampleGenerator();

        genre.addNovel(novelBack);
        assertThat(genre.getNovels()).containsOnly(novelBack);
        assertThat(novelBack.getGenres()).containsOnly(genre);

        genre.removeNovel(novelBack);
        assertThat(genre.getNovels()).doesNotContain(novelBack);
        assertThat(novelBack.getGenres()).doesNotContain(genre);

        genre.novels(new HashSet<>(Set.of(novelBack)));
        assertThat(genre.getNovels()).containsOnly(novelBack);
        assertThat(novelBack.getGenres()).containsOnly(genre);

        genre.setNovels(new HashSet<>());
        assertThat(genre.getNovels()).doesNotContain(novelBack);
        assertThat(novelBack.getGenres()).doesNotContain(genre);
    }
}
