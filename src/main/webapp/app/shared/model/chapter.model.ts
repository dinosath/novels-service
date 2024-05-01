import { INovel } from 'app/shared/model/novel.model';

export interface IChapter {
  id?: number;
  title?: string;
  content?: string;
  novel?: INovel | null;
}

export const defaultValue: Readonly<IChapter> = {};
