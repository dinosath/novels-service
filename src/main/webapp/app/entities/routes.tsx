import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Novel from './novel';
import Genre from './genre';
import Tag from './tag';
import Author from './author';
import Chapter from './chapter';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="novel/*" element={<Novel />} />
        <Route path="genre/*" element={<Genre />} />
        <Route path="tag/*" element={<Tag />} />
        <Route path="author/*" element={<Author />} />
        <Route path="chapter/*" element={<Chapter />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
