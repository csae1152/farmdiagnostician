import { animalKindTypeSearch } from 'app/shared/model/enumerations/animal-kind-type-search.model';

export interface IRetina {
  id?: number;
  name?: string;
  pictureContentType?: string;
  picture?: any;
  animal?: animalKindTypeSearch;
}

export const defaultValue: Readonly<IRetina> = {};
