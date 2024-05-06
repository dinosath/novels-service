import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chapter.reducer';

export const ChapterDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chapterEntity = useAppSelector(state => state.chapter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chapterDetailsHeading">
          <Translate contentKey="novelsApp.chapter.detail.title">Chapter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chapterEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="novelsApp.chapter.title">Title</Translate>
            </span>
          </dt>
          <dd>{chapterEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="novelsApp.chapter.content">Content</Translate>
            </span>
          </dt>
          <dd>{chapterEntity.content}</dd>
          <dt>
            <Translate contentKey="novelsApp.chapter.novel">Novel</Translate>
          </dt>
          <dd>{chapterEntity.novel ? chapterEntity.novel.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/chapter" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chapter/${chapterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChapterDetail;
