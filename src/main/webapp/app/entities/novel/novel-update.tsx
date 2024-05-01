import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGenre } from 'app/shared/model/genre.model';
import { getEntities as getGenres } from 'app/entities/genre/genre.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IAuthor } from 'app/shared/model/author.model';
import { getEntities as getAuthors } from 'app/entities/author/author.reducer';
import { INovel } from 'app/shared/model/novel.model';
import { getEntity, updateEntity, createEntity, reset } from './novel.reducer';

export const NovelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const genres = useAppSelector(state => state.genre.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const authors = useAppSelector(state => state.author.entities);
  const novelEntity = useAppSelector(state => state.novel.entity);
  const loading = useAppSelector(state => state.novel.loading);
  const updating = useAppSelector(state => state.novel.updating);
  const updateSuccess = useAppSelector(state => state.novel.updateSuccess);

  const handleClose = () => {
    navigate('/novel' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGenres({}));
    dispatch(getTags({}));
    dispatch(getAuthors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...novelEntity,
      ...values,
      genres: mapIdList(values.genres),
      tags: mapIdList(values.tags),
      authors: mapIdList(values.authors),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...novelEntity,
          genres: novelEntity?.genres?.map(e => e.id.toString()),
          tags: novelEntity?.tags?.map(e => e.id.toString()),
          authors: novelEntity?.authors?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="novelsApp.novel.home.createOrEditLabel" data-cy="NovelCreateUpdateHeading">
            <Translate contentKey="novelsApp.novel.home.createOrEditLabel">Create or edit a Novel</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="novel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('novelsApp.novel.title')}
                id="novel-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('novelsApp.novel.genre')}
                id="novel-genre"
                data-cy="genre"
                type="select"
                multiple
                name="genres"
              >
                <option value="" key="0" />
                {genres
                  ? genres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label={translate('novelsApp.novel.tag')} id="novel-tag" data-cy="tag" type="select" multiple name="tags">
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('novelsApp.novel.author')}
                id="novel-author"
                data-cy="author"
                type="select"
                multiple
                name="authors"
              >
                <option value="" key="0" />
                {authors
                  ? authors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/novel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NovelUpdate;
