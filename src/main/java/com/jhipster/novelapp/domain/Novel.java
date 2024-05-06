package com.jhipster.novelapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Novel.
 */
@Entity
@Table(name = "novel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Novel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "novel" }, allowSetters = true)
    private Set<Chapter> chapters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_novel__genre", joinColumns = @JoinColumn(name = "novel_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "novels" }, allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_novel__tag", joinColumns = @JoinColumn(name = "novel_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "novels" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_novel__author",
        joinColumns = @JoinColumn(name = "novel_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "novels" }, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Novel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Novel title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Chapter> getChapters() {
        return this.chapters;
    }

    public void setChapters(Set<Chapter> chapters) {
        if (this.chapters != null) {
            this.chapters.forEach(i -> i.setNovel(null));
        }
        if (chapters != null) {
            chapters.forEach(i -> i.setNovel(this));
        }
        this.chapters = chapters;
    }

    public Novel chapters(Set<Chapter> chapters) {
        this.setChapters(chapters);
        return this;
    }

    public Novel addChapter(Chapter chapter) {
        this.chapters.add(chapter);
        chapter.setNovel(this);
        return this;
    }

    public Novel removeChapter(Chapter chapter) {
        this.chapters.remove(chapter);
        chapter.setNovel(null);
        return this;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Novel genres(Set<Genre> genres) {
        this.setGenres(genres);
        return this;
    }

    public Novel addGenre(Genre genre) {
        this.genres.add(genre);
        return this;
    }

    public Novel removeGenre(Genre genre) {
        this.genres.remove(genre);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Novel tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Novel addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Novel removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Novel authors(Set<Author> authors) {
        this.setAuthors(authors);
        return this;
    }

    public Novel addAuthor(Author author) {
        this.authors.add(author);
        return this;
    }

    public Novel removeAuthor(Author author) {
        this.authors.remove(author);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Novel)) {
            return false;
        }
        return getId() != null && getId().equals(((Novel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Novel{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
