package com.farmdiagnostician.web.web.rest;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.mxnet.zoo.MxModelZoo;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import com.farmdiagnostician.web.service.RetinaQueryService;
import com.farmdiagnostician.web.service.RetinaService;
import com.farmdiagnostician.web.service.dto.RetinaCriteria;
import com.farmdiagnostician.web.service.dto.RetinaDTO;
import com.farmdiagnostician.web.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.farmdiagnostician.web.domain.Retina}.
 */
@RestController
@RequestMapping("/api")
public class RetinaResource {
    private static final String ENTITY_NAME = "retina";
    private final Logger log = LoggerFactory.getLogger(RetinaResource.class);
    private final RetinaService retinaService;
    private final RetinaQueryService retinaQueryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public RetinaResource(RetinaService retinaService, RetinaQueryService retinaQueryService) {
        this.retinaService = retinaService;
        this.retinaQueryService = retinaQueryService;
    }

    /**
     * {@code POST  /retinas} : Create a new retina.
     *
     * @param retinaDTO the retinaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retinaDTO, or with status {@code 400 (Bad Request)} if the retina has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retinas")
    public ResponseEntity<RetinaDTO> createRetina(@RequestBody RetinaDTO retinaDTO) throws URISyntaxException {
        log.debug("REST request to save Retina : {}", retinaDTO);
        if (retinaDTO.getId() != null) {
            throw new BadRequestAlertException("A new retina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetinaDTO result = retinaService.save(retinaDTO);
        return ResponseEntity
            .created(new URI("/api/retinas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retinas} : Updates an existing retina.
     *
     * @param retinaDTO the retinaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retinaDTO,
     * or with status {@code 400 (Bad Request)} if the retinaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retinaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retinas")
    public ResponseEntity<RetinaDTO> updateRetina(@RequestBody RetinaDTO retinaDTO) throws URISyntaxException {
        log.debug("REST request to update Retina : {}", retinaDTO);
        if (retinaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RetinaDTO result = retinaService.save(retinaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retinaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /retinas} : get all the retinas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retinas in body.
     */
    @GetMapping("/retinas")
    public ResponseEntity<List<RetinaDTO>> getAllRetinas(RetinaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Retinas by criteria: {}", criteria);
        Page<RetinaDTO> page = retinaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /retinas/count} : count all the retinas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/retinas/count")
    public ResponseEntity<Long> countRetinas(RetinaCriteria criteria) {
        log.debug("REST request to count Retinas by criteria: {}", criteria);
        return ResponseEntity.ok().body(retinaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /retinas/:id} : get the "id" retina.
     *
     * @param id the id of the retinaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retinaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retinas/{id}")
    public ResponseEntity<RetinaDTO> getRetina(@PathVariable Long id) {
        log.debug("REST request to get Retina : {}", id);
        Optional<RetinaDTO> retinaDTO = retinaService.findOne(id);

        ByteArrayInputStream bais = new ByteArrayInputStream(retinaDTO.get().getPicture());
        /**
         * load prediction model.
         */
        try (ZooModel<Image, Classifications> model = MxModelZoo.RESNET.loadModel()) {
            try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
                Image input = ImageFactory.getInstance().fromInputStream(bais);
                Classifications detection = predictor.predict(input);
                var result = detection.best().getClassName();
                var substring = result.substring(0, 9);
                var print = result.replace(substring, "");
                retinaDTO.get().setName(print);
            }
        } catch (MalformedModelException e) {
            e.printStackTrace();
        } catch (ModelNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | TranslateException e) {
            e.printStackTrace();
        }
        return ResponseUtil.wrapOrNotFound(retinaDTO);
    }

    /**
     * {@code DELETE  /retinas/:id} : delete the "id" retina.
     *
     * @param id the id of the retinaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retinas/{id}")
    public ResponseEntity<Void> deleteRetina(@PathVariable Long id) {
        log.debug("REST request to delete Retina : {}", id);
        retinaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
