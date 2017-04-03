package com.theironyard.novauc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.theironyard.novauc.domain.Todoitem;

import com.theironyard.novauc.repository.TodoitemRepository;
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
 * REST controller for managing Todoitem.
 */
@RestController
@RequestMapping("/api")
public class TodoitemResource {

    private final Logger log = LoggerFactory.getLogger(TodoitemResource.class);

    private static final String ENTITY_NAME = "todoitem";
        
    private final TodoitemRepository todoitemRepository;

    public TodoitemResource(TodoitemRepository todoitemRepository) {
        this.todoitemRepository = todoitemRepository;
    }

    /**
     * POST  /todoitems : Create a new todoitem.
     *
     * @param todoitem the todoitem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new todoitem, or with status 400 (Bad Request) if the todoitem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/todoitems")
    @Timed
    public ResponseEntity<Todoitem> createTodoitem(@Valid @RequestBody Todoitem todoitem) throws URISyntaxException {
        log.debug("REST request to save Todoitem : {}", todoitem);
        if (todoitem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new todoitem cannot already have an ID")).body(null);
        }
        Todoitem result = todoitemRepository.save(todoitem);
        return ResponseEntity.created(new URI("/api/todoitems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /todoitems : Updates an existing todoitem.
     *
     * @param todoitem the todoitem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated todoitem,
     * or with status 400 (Bad Request) if the todoitem is not valid,
     * or with status 500 (Internal Server Error) if the todoitem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/todoitems")
    @Timed
    public ResponseEntity<Todoitem> updateTodoitem(@Valid @RequestBody Todoitem todoitem) throws URISyntaxException {
        log.debug("REST request to update Todoitem : {}", todoitem);
        if (todoitem.getId() == null) {
            return createTodoitem(todoitem);
        }
        Todoitem result = todoitemRepository.save(todoitem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, todoitem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /todoitems : get all the todoitems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of todoitems in body
     */
    @GetMapping("/todoitems")
    @Timed
    public List<Todoitem> getAllTodoitems() {
        log.debug("REST request to get all Todoitems");
        List<Todoitem> todoitems = todoitemRepository.findAll();
        return todoitems;
    }

    /**
     * GET  /todoitems/:id : get the "id" todoitem.
     *
     * @param id the id of the todoitem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the todoitem, or with status 404 (Not Found)
     */
    @GetMapping("/todoitems/{id}")
    @Timed
    public ResponseEntity<Todoitem> getTodoitem(@PathVariable Long id) {
        log.debug("REST request to get Todoitem : {}", id);
        Todoitem todoitem = todoitemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(todoitem));
    }

    /**
     * DELETE  /todoitems/:id : delete the "id" todoitem.
     *
     * @param id the id of the todoitem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/todoitems/{id}")
    @Timed
    public ResponseEntity<Void> deleteTodoitem(@PathVariable Long id) {
        log.debug("REST request to delete Todoitem : {}", id);
        todoitemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
