package com.jhipster.novelapp.domain;

import static com.jhipster.novelapp.domain.AuthorTestSamples.*;
import static com.jhipster.novelapp.domain.NovelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void novelTest() throws Exception {
        Author author = getAuthorRandomSampleGenerator();
        Novel novelBack = getNovelRandomSampleGenerator();

        author.addNovel(novelBack);
        assertThat(author.getNovels()).containsOnly(novelBack);
        assertThat(novelBack.getAuthors()).containsOnly(author);

        author.removeNovel(novelBack);
        assertThat(author.getNovels()).doesNotContain(novelBack);
        assertThat(novelBack.getAuthors()).doesNotContain(author);

        author.novels(new HashSet<>(Set.of(novelBack)));
        assertThat(author.getNovels()).containsOnly(novelBack);
        assertThat(novelBack.getAuthors()).containsOnly(author);

        author.setNovels(new HashSet<>());
        assertThat(author.getNovels()).doesNotContain(novelBack);
        assertThat(novelBack.getAuthors()).doesNotContain(author);
    }
}
