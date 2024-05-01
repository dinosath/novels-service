import { INovel } from 'app/shared/model/novel.model';

export interface IAuthor {
  id?: number;
  name?: string;
  novels?: INovel[] | null;
}

export const defaultValue: Readonly<IAuthor> = {};
