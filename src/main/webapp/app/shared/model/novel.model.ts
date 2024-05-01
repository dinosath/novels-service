import { IChapter } from 'app/shared/model/chapter.model';
import { IGenre } from 'app/shared/model/genre.model';
import { ITag } from 'app/shared/model/tag.model';
import { IAuthor } from 'app/shared/model/author.model';

export interface INovel {
  id?: number;
  title?: string;
  chapters?: IChapter[] | null;
  genres?: IGenre[] | null;
  tags?: ITag[] | null;
  authors?: IAuthor[] | null;
}

export const defaultValue: Readonly<INovel> = {};
