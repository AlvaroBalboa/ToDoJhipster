package com.theironyard.novauc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.theironyard.novauc.domain.Citizen;

import com.theironyard.novauc.repository.CitizenRepository;
import com.theironyard.novauc.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Citizen.
 */
@RestController
@RequestMapping("/api")
public class CitizenResource {

    private final Logger log = LoggerFactory.getLogger(CitizenResource.class);

    private static final String ENTITY_NAME = "citizen";
        
    private final CitizenRepository citizenRepository;

    public CitizenResource(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    /**
     * POST  /citizens : Create a new citizen.
     *
     * @param citizen the citizen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new citizen, or with status 400 (Bad Request) if the citizen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/citizens")
    @Timed
    public ResponseEntity<Citizen> createCitizen(@Valid @RequestBody Citizen citizen) throws URISyntaxException {
        log.debug("REST request to save Citizen : {}", citizen);
        if (citizen.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new citizen cannot already have an ID")).body(null);
        }
        Citizen result = citizenRepository.save(citizen);
        return ResponseEntity.created(new URI("/api/citizens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /citizens : Updates an existing citizen.
     *
     * @param citizen the citizen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated citizen,
     * or with status 400 (Bad Request) if the citizen is not valid,
     * or with status 500 (Internal Server Error) if the citizen couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/citizens")
    @Timed
    public ResponseEntity<Citizen> updateCitizen(@Valid @RequestBody Citizen citizen) throws URISyntaxException {
        log.debug("REST request to update Citizen : {}", citizen);
        if (citizen.getId() == null) {
            return createCitizen(citizen);
        }
        Citizen result = citizenRepository.save(citizen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, citizen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /citizens : get all the citizens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of citizens in body
     */
    @GetMapping("/citizens")
    @Timed
    public List<Citizen> getAllCitizens() {
        log.debug("REST request to get all Citizens");
        List<Citizen> citizens = citizenRepository.findAll();
        return citizens;
    }

    /**
     * GET  /citizens/:id : get the "id" citizen.
     *
     * @param id the id of the citizen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the citizen, or with status 404 (Not Found)
     */
    @GetMapping("/citizens/{id}")
    @Timed
    public ResponseEntity<Citizen> getCitizen(@PathVariable Long id) {
        log.debug("REST request to get Citizen : {}", id);
        Citizen citizen = citizenRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(citizen));
    }

    /**
     * DELETE  /citizens/:id : delete the "id" citizen.
     *
     * @param id the id of the citizen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/citizens/{id}")
    @Timed
    public ResponseEntity<Void> deleteCitizen(@PathVariable Long id) {
        log.debug("REST request to delete Citizen : {}", id);
        citizenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
