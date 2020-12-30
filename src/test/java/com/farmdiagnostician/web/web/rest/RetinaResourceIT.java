package com.farmdiagnostician.web.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.farmdiagnostician.web.FarmdiagnosticianApp;
import com.farmdiagnostician.web.domain.Retina;
import com.farmdiagnostician.web.domain.enumeration.animalKindTypeSearch;
import com.farmdiagnostician.web.repository.RetinaRepository;
import com.farmdiagnostician.web.service.RetinaQueryService;
import com.farmdiagnostician.web.service.RetinaService;
import com.farmdiagnostician.web.service.dto.RetinaDTO;
import com.farmdiagnostician.web.service.mapper.RetinaMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RetinaResource} REST controller.
 */
@SpringBootTest(classes = FarmdiagnosticianApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RetinaResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final animalKindTypeSearch DEFAULT_ANIMAL = animalKindTypeSearch.COW;
    private static final animalKindTypeSearch UPDATED_ANIMAL = animalKindTypeSearch.PIG;

    @Autowired
    private RetinaRepository retinaRepository;

    @Autowired
    private RetinaMapper retinaMapper;

    @Autowired
    private RetinaService retinaService;

    @Autowired
    private RetinaQueryService retinaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetinaMockMvc;

    private Retina retina;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retina createEntity(EntityManager em) {
        Retina retina = new Retina()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .animal(DEFAULT_ANIMAL);
        return retina;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retina createUpdatedEntity(EntityManager em) {
        Retina retina = new Retina()
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .animal(UPDATED_ANIMAL);
        return retina;
    }

    @BeforeEach
    public void initTest() {
        retina = createEntity(em);
    }

    @Test
    @Transactional
    public void createRetina() throws Exception {
        int databaseSizeBeforeCreate = retinaRepository.findAll().size();
        // Create the Retina
        RetinaDTO retinaDTO = retinaMapper.toDto(retina);
        restRetinaMockMvc
            .perform(post("/api/retinas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retinaDTO)))
            .andExpect(status().isCreated());

        // Validate the Retina in the database
        List<Retina> retinaList = retinaRepository.findAll();
        assertThat(retinaList).hasSize(databaseSizeBeforeCreate + 1);
        Retina testRetina = retinaList.get(retinaList.size() - 1);
        assertThat(testRetina.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRetina.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testRetina.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testRetina.getAnimal()).isEqualTo(DEFAULT_ANIMAL);
    }

    @Test
    @Transactional
    public void createRetinaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = retinaRepository.findAll().size();

        // Create the Retina with an existing ID
        retina.setId(1L);
        RetinaDTO retinaDTO = retinaMapper.toDto(retina);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetinaMockMvc
            .perform(post("/api/retinas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Retina in the database
        List<Retina> retinaList = retinaRepository.findAll();
        assertThat(retinaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRetinas() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList
        restRetinaMockMvc
            .perform(get("/api/retinas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retina.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].animal").value(hasItem(DEFAULT_ANIMAL.toString())));
    }

    @Test
    @Transactional
    public void getRetina() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get the retina
        restRetinaMockMvc
            .perform(get("/api/retinas/{id}", retina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retina.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.animal").value(DEFAULT_ANIMAL.toString()));
    }

    @Test
    @Transactional
    public void getRetinasByIdFiltering() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        Long id = retina.getId();

        defaultRetinaShouldBeFound("id.equals=" + id);
        defaultRetinaShouldNotBeFound("id.notEquals=" + id);

        defaultRetinaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRetinaShouldNotBeFound("id.greaterThan=" + id);

        defaultRetinaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRetinaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllRetinasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where name equals to DEFAULT_NAME
        defaultRetinaShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the retinaList where name equals to UPDATED_NAME
        defaultRetinaShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRetinasByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where name not equals to DEFAULT_NAME
        defaultRetinaShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the retinaList where name not equals to UPDATED_NAME
        defaultRetinaShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRetinasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRetinaShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the retinaList where name equals to UPDATED_NAME
        defaultRetinaShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRetinasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where name is not null
        defaultRetinaShouldBeFound("name.specified=true");

        // Get all the retinaList where name is null
        defaultRetinaShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRetinasByNameContainsSomething() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where name contains DEFAULT_NAME
        defaultRetinaShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the retinaList where name contains UPDATED_NAME
        defaultRetinaShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRetinasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where name does not contain DEFAULT_NAME
        defaultRetinaShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the retinaList where name does not contain UPDATED_NAME
        defaultRetinaShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRetinasByAnimalIsEqualToSomething() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where animal equals to DEFAULT_ANIMAL
        defaultRetinaShouldBeFound("animal.equals=" + DEFAULT_ANIMAL);

        // Get all the retinaList where animal equals to UPDATED_ANIMAL
        defaultRetinaShouldNotBeFound("animal.equals=" + UPDATED_ANIMAL);
    }

    @Test
    @Transactional
    public void getAllRetinasByAnimalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where animal not equals to DEFAULT_ANIMAL
        defaultRetinaShouldNotBeFound("animal.notEquals=" + DEFAULT_ANIMAL);

        // Get all the retinaList where animal not equals to UPDATED_ANIMAL
        defaultRetinaShouldBeFound("animal.notEquals=" + UPDATED_ANIMAL);
    }

    @Test
    @Transactional
    public void getAllRetinasByAnimalIsInShouldWork() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where animal in DEFAULT_ANIMAL or UPDATED_ANIMAL
        defaultRetinaShouldBeFound("animal.in=" + DEFAULT_ANIMAL + "," + UPDATED_ANIMAL);

        // Get all the retinaList where animal equals to UPDATED_ANIMAL
        defaultRetinaShouldNotBeFound("animal.in=" + UPDATED_ANIMAL);
    }

    @Test
    @Transactional
    public void getAllRetinasByAnimalIsNullOrNotNull() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        // Get all the retinaList where animal is not null
        defaultRetinaShouldBeFound("animal.specified=true");

        // Get all the retinaList where animal is null
        defaultRetinaShouldNotBeFound("animal.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRetinaShouldBeFound(String filter) throws Exception {
        restRetinaMockMvc
            .perform(get("/api/retinas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retina.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].animal").value(hasItem(DEFAULT_ANIMAL.toString())));

        // Check, that the count call also returns 1
        restRetinaMockMvc
            .perform(get("/api/retinas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRetinaShouldNotBeFound(String filter) throws Exception {
        restRetinaMockMvc
            .perform(get("/api/retinas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRetinaMockMvc
            .perform(get("/api/retinas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRetina() throws Exception {
        // Get the retina
        restRetinaMockMvc.perform(get("/api/retinas/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRetina() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        int databaseSizeBeforeUpdate = retinaRepository.findAll().size();

        // Update the retina
        Retina updatedRetina = retinaRepository.findById(retina.getId()).get();
        // Disconnect from session so that the updates on updatedRetina are not directly saved in db
        em.detach(updatedRetina);
        updatedRetina.name(UPDATED_NAME).picture(UPDATED_PICTURE).pictureContentType(UPDATED_PICTURE_CONTENT_TYPE).animal(UPDATED_ANIMAL);
        RetinaDTO retinaDTO = retinaMapper.toDto(updatedRetina);

        restRetinaMockMvc
            .perform(put("/api/retinas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retinaDTO)))
            .andExpect(status().isOk());

        // Validate the Retina in the database
        List<Retina> retinaList = retinaRepository.findAll();
        assertThat(retinaList).hasSize(databaseSizeBeforeUpdate);
        Retina testRetina = retinaList.get(retinaList.size() - 1);
        assertThat(testRetina.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRetina.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testRetina.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testRetina.getAnimal()).isEqualTo(UPDATED_ANIMAL);
    }

    @Test
    @Transactional
    public void updateNonExistingRetina() throws Exception {
        int databaseSizeBeforeUpdate = retinaRepository.findAll().size();

        // Create the Retina
        RetinaDTO retinaDTO = retinaMapper.toDto(retina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetinaMockMvc
            .perform(put("/api/retinas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Retina in the database
        List<Retina> retinaList = retinaRepository.findAll();
        assertThat(retinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRetina() throws Exception {
        // Initialize the database
        retinaRepository.saveAndFlush(retina);

        int databaseSizeBeforeDelete = retinaRepository.findAll().size();

        // Delete the retina
        restRetinaMockMvc
            .perform(delete("/api/retinas/{id}", retina.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Retina> retinaList = retinaRepository.findAll();
        assertThat(retinaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
