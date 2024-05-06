package com.jhipster.novelapp.domain;

import static com.jhipster.novelapp.domain.AuthorTestSamples.*;
import static com.jhipster.novelapp.domain.ChapterTestSamples.*;
import static com.jhipster.novelapp.domain.GenreTestSamples.*;
import static com.jhipster.novelapp.domain.NovelTestSamples.*;
import static com.jhipster.novelapp.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NovelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Novel.class);
        Novel novel1 = getNovelSample1();
        Novel novel2 = new Novel();
        assertThat(novel1).isNotEqualTo(novel2);

        novel2.setId(novel1.getId());
        assertThat(novel1).isEqualTo(novel2);

        novel2 = getNovelSample2();
        assertThat(novel1).isNotEqualTo(novel2);
    }

    @Test
    void chapterTest() throws Exception {
        Novel novel = getNovelRandomSampleGenerator();
        Chapter chapterBack = getChapterRandomSampleGenerator();

        novel.addChapter(chapterBack);
        assertThat(novel.getChapters()).containsOnly(chapterBack);
        assertThat(chapterBack.getNovel()).isEqualTo(novel);

        novel.removeChapter(chapterBack);
        assertThat(novel.getChapters()).doesNotContain(chapterBack);
        assertThat(chapterBack.getNovel()).isNull();

        novel.chapters(new HashSet<>(Set.of(chapterBack)));
        assertThat(novel.getChapters()).containsOnly(chapterBack);
        assertThat(chapterBack.getNovel()).isEqualTo(novel);

        novel.setChapters(new HashSet<>());
        assertThat(novel.getChapters()).doesNotContain(chapterBack);
        assertThat(chapterBack.getNovel()).isNull();
    }

    @Test
    void genreTest() throws Exception {
        Novel novel = getNovelRandomSampleGenerator();
        Genre genreBack = getGenreRandomSampleGenerator();

        novel.addGenre(genreBack);
        assertThat(novel.getGenres()).containsOnly(genreBack);

        novel.removeGenre(genreBack);
        assertThat(novel.getGenres()).doesNotContain(genreBack);

        novel.genres(new HashSet<>(Set.of(genreBack)));
        assertThat(novel.getGenres()).containsOnly(genreBack);

        novel.setGenres(new HashSet<>());
        assertThat(novel.getGenres()).doesNotContain(genreBack);
    }

    @Test
    void tagTest() throws Exception {
        Novel novel = getNovelRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        novel.addTag(tagBack);
        assertThat(novel.getTags()).containsOnly(tagBack);

        novel.removeTag(tagBack);
        assertThat(novel.getTags()).doesNotContain(tagBack);

        novel.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(novel.getTags()).containsOnly(tagBack);

        novel.setTags(new HashSet<>());
        assertThat(novel.getTags()).doesNotContain(tagBack);
    }

    @Test
    void authorTest() throws Exception {
        Novel novel = getNovelRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        novel.addAuthor(authorBack);
        assertThat(novel.getAuthors()).containsOnly(authorBack);

        novel.removeAuthor(authorBack);
        assertThat(novel.getAuthors()).doesNotContain(authorBack);

        novel.authors(new HashSet<>(Set.of(authorBack)));
        assertThat(novel.getAuthors()).containsOnly(authorBack);

        novel.setAuthors(new HashSet<>());
        assertThat(novel.getAuthors()).doesNotContain(authorBack);
    }
}
