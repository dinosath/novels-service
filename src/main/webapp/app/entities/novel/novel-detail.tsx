import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './novel.reducer';

export const NovelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const novelEntity = useAppSelector(state => state.novel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="novelDetailsHeading">
          <Translate contentKey="novelsApp.novel.detail.title">Novel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{novelEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="novelsApp.novel.title">Title</Translate>
            </span>
          </dt>
          <dd>{novelEntity.title}</dd>
          <dt>
            <Translate contentKey="novelsApp.novel.genre">Genre</Translate>
          </dt>
          <dd>
            {novelEntity.genres
              ? novelEntity.genres.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {novelEntity.genres && i === novelEntity.genres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="novelsApp.novel.tag">Tag</Translate>
          </dt>
          <dd>
            {novelEntity.tags
              ? novelEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {novelEntity.tags && i === novelEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="novelsApp.novel.author">Author</Translate>
          </dt>
          <dd>
            {novelEntity.authors
              ? novelEntity.authors.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {novelEntity.authors && i === novelEntity.authors.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/novel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/novel/${novelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NovelDetail;
