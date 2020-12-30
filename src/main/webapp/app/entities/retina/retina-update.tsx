import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './retina.reducer';
import { IRetina } from 'app/shared/model/retina.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRetinaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RetinaUpdate = (props: IRetinaUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { retinaEntity, loading, updating } = props;

  const { picture, pictureContentType } = retinaEntity;

  const handleClose = () => {
    props.history.push('/retina' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...retinaEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="farmdiagnosticianApp.retina.home.createOrEditLabel">
            <Translate contentKey="farmdiagnosticianApp.retina.home.createOrEditLabel">Create or edit a Retina</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : retinaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="retina-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="retina-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="retina-name">
                  <Translate contentKey="farmdiagnosticianApp.retina.name">Name</Translate>
                </Label>
                <AvField id="retina-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="pictureLabel" for="picture">
                    <Translate contentKey="farmdiagnosticianApp.retina.picture">Picture</Translate>
                  </Label>
                  <br />
                  {picture ? (
                    <div>
                      {pictureContentType ? (
                        <a onClick={openFile(pictureContentType, picture)}>
                          <img src={`data:${pictureContentType};base64,${picture}`} style={{ maxHeight: '100px' }} />
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {pictureContentType}, {byteSize(picture)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('picture')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_picture" type="file" onChange={onBlobChange(true, 'picture')} accept="image/*" />
                  <AvInput type="hidden" name="picture" value={picture} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label id="animalLabel" for="retina-animal">
                  <Translate contentKey="farmdiagnosticianApp.retina.animal">Animal</Translate>
                </Label>
                <AvInput
                  id="retina-animal"
                  type="select"
                  className="form-control"
                  name="animal"
                  value={(!isNew && retinaEntity.animal) || 'COW'}
                >
                  <option value="COW">{translate('farmdiagnosticianApp.animalKindTypeSearch.COW')}</option>
                  <option value="PIG">{translate('farmdiagnosticianApp.animalKindTypeSearch.PIG')}</option>
                  <option value="GOAT">{translate('farmdiagnosticianApp.animalKindTypeSearch.GOAT')}</option>
                  <option value="CHICKEN">{translate('farmdiagnosticianApp.animalKindTypeSearch.CHICKEN')}</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/retina" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  retinaEntity: storeState.retina.entity,
  loading: storeState.retina.loading,
  updating: storeState.retina.updating,
  updateSuccess: storeState.retina.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RetinaUpdate);
