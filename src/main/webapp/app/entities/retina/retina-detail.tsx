import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './retina.reducer';
import { IRetina } from 'app/shared/model/retina.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRetinaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RetinaDetail = (props: IRetinaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { retinaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="farmdiagnosticianApp.retina.detail.title">Retina</Translate> [<b>{retinaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="farmdiagnosticianApp.retina.name">Name</Translate>
            </span>
          </dt>
          <dd>{retinaEntity.name}</dd>
          <dt>
            <span id="picture">
              <Translate contentKey="farmdiagnosticianApp.retina.picture">Picture</Translate>
            </span>
          </dt>
          <dd>
            {retinaEntity.picture ? (
              <div>
                {retinaEntity.pictureContentType ? (
                  <a onClick={openFile(retinaEntity.pictureContentType, retinaEntity.picture)}>
                    <img src={`data:${retinaEntity.pictureContentType};base64,${retinaEntity.picture}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {retinaEntity.pictureContentType}, {byteSize(retinaEntity.picture)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="animal">
              <Translate contentKey="farmdiagnosticianApp.retina.animal">Animal</Translate>
            </span>
          </dt>
          <dd>{retinaEntity.animal}</dd>
        </dl>
        <Button tag={Link} to="/retina" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/retina/${retinaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ retina }: IRootState) => ({
  retinaEntity: retina.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RetinaDetail);
