import novel from 'app/entities/novel/novel.reducer';
import genre from 'app/entities/genre/genre.reducer';
import tag from 'app/entities/tag/tag.reducer';
import author from 'app/entities/author/author.reducer';
import chapter from 'app/entities/chapter/chapter.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  novel,
  genre,
  tag,
  author,
  chapter,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
