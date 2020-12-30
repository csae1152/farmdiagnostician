import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRetina, defaultValue } from 'app/shared/model/retina.model';

export const ACTION_TYPES = {
  FETCH_RETINA_LIST: 'retina/FETCH_RETINA_LIST',
  FETCH_RETINA: 'retina/FETCH_RETINA',
  CREATE_RETINA: 'retina/CREATE_RETINA',
  UPDATE_RETINA: 'retina/UPDATE_RETINA',
  DELETE_RETINA: 'retina/DELETE_RETINA',
  SET_BLOB: 'retina/SET_BLOB',
  RESET: 'retina/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRetina>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type RetinaState = Readonly<typeof initialState>;

// Reducer

export default (state: RetinaState = initialState, action): RetinaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RETINA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RETINA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_RETINA):
    case REQUEST(ACTION_TYPES.UPDATE_RETINA):
    case REQUEST(ACTION_TYPES.DELETE_RETINA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_RETINA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RETINA):
    case FAILURE(ACTION_TYPES.CREATE_RETINA):
    case FAILURE(ACTION_TYPES.UPDATE_RETINA):
    case FAILURE(ACTION_TYPES.DELETE_RETINA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RETINA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_RETINA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_RETINA):
    case SUCCESS(ACTION_TYPES.UPDATE_RETINA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_RETINA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/retinas';

// Actions

export const getEntities: ICrudGetAllAction<IRetina> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RETINA_LIST,
    payload: axios.get<IRetina>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IRetina> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RETINA,
    payload: axios.get<IRetina>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRetina> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RETINA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRetina> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RETINA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRetina> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RETINA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
