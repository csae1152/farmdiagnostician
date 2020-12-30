import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Retina from './retina';
import RetinaDetail from './retina-detail';
import RetinaUpdate from './retina-update';
import RetinaDeleteDialog from './retina-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RetinaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RetinaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RetinaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Retina} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RetinaDeleteDialog} />
  </>
);

export default Routes;
