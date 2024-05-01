import { INovel } from 'app/shared/model/novel.model';

export interface IGenre {
  id?: number;
  name?: string;
  novels?: INovel[] | null;
}

export const defaultValue: Readonly<IGenre> = {};
