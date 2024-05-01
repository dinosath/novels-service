import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Novel from './novel';
import NovelDetail from './novel-detail';
import NovelUpdate from './novel-update';
import NovelDeleteDialog from './novel-delete-dialog';

const NovelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Novel />} />
    <Route path="new" element={<NovelUpdate />} />
    <Route path=":id">
      <Route index element={<NovelDetail />} />
      <Route path="edit" element={<NovelUpdate />} />
      <Route path="delete" element={<NovelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NovelRoutes;
