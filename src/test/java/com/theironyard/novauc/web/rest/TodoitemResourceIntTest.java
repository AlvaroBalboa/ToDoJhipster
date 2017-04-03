package com.theironyard.novauc.web.rest;

import com.theironyard.novauc.ToDoJhipsterApp;

import com.theironyard.novauc.domain.Todoitem;
import com.theironyard.novauc.repository.TodoitemRepository;
import com.theironyard.novauc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TodoitemResource REST controller.
 *
 * @see TodoitemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToDoJhipsterApp.class)
public class TodoitemResourceIntTest {

    private static final String DEFAULT_TODO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TODO_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_DONE = false;
    private static final Boolean UPDATED_IS_DONE = true;

    private static final LocalDate DEFAULT_DATE_DUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DUE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TodoitemRepository todoitemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTodoitemMockMvc;

    private Todoitem todoitem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TodoitemResource todoitemResource = new TodoitemResource(todoitemRepository);
        this.restTodoitemMockMvc = MockMvcBuilders.standaloneSetup(todoitemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todoitem createEntity(EntityManager em) {
        Todoitem todoitem = new Todoitem()
            .todoName(DEFAULT_TODO_NAME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .isDone(DEFAULT_IS_DONE)
            .dateDue(DEFAULT_DATE_DUE);
        return todoitem;
    }

    @Before
    public void initTest() {
        todoitem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTodoitem() throws Exception {
        int databaseSizeBeforeCreate = todoitemRepository.findAll().size();

        // Create the Todoitem
        restTodoitemMockMvc.perform(post("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoitem)))
            .andExpect(status().isCreated());

        // Validate the Todoitem in the database
        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeCreate + 1);
        Todoitem testTodoitem = todoitemList.get(todoitemList.size() - 1);
        assertThat(testTodoitem.getTodoName()).isEqualTo(DEFAULT_TODO_NAME);
        assertThat(testTodoitem.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testTodoitem.isIsDone()).isEqualTo(DEFAULT_IS_DONE);
        assertThat(testTodoitem.getDateDue()).isEqualTo(DEFAULT_DATE_DUE);
    }

    @Test
    @Transactional
    public void createTodoitemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = todoitemRepository.findAll().size();

        // Create the Todoitem with an existing ID
        todoitem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoitemMockMvc.perform(post("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoitem)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoitemRepository.findAll().size();
        // set the field null
        todoitem.setDateCreated(null);

        // Create the Todoitem, which fails.

        restTodoitemMockMvc.perform(post("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoitem)))
            .andExpect(status().isBadRequest());

        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoitemRepository.findAll().size();
        // set the field null
        todoitem.setIsDone(null);

        // Create the Todoitem, which fails.

        restTodoitemMockMvc.perform(post("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoitem)))
            .andExpect(status().isBadRequest());

        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDueIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoitemRepository.findAll().size();
        // set the field null
        todoitem.setDateDue(null);

        // Create the Todoitem, which fails.

        restTodoitemMockMvc.perform(post("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoitem)))
            .andExpect(status().isBadRequest());

        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTodoitems() throws Exception {
        // Initialize the database
        todoitemRepository.saveAndFlush(todoitem);

        // Get all the todoitemList
        restTodoitemMockMvc.perform(get("/api/todoitems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todoitem.getId().intValue())))
            .andExpect(jsonPath("$.[*].todoName").value(hasItem(DEFAULT_TODO_NAME.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].isDone").value(hasItem(DEFAULT_IS_DONE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDue").value(hasItem(DEFAULT_DATE_DUE.toString())));
    }

    @Test
    @Transactional
    public void getTodoitem() throws Exception {
        // Initialize the database
        todoitemRepository.saveAndFlush(todoitem);

        // Get the todoitem
        restTodoitemMockMvc.perform(get("/api/todoitems/{id}", todoitem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(todoitem.getId().intValue()))
            .andExpect(jsonPath("$.todoName").value(DEFAULT_TODO_NAME.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.isDone").value(DEFAULT_IS_DONE.booleanValue()))
            .andExpect(jsonPath("$.dateDue").value(DEFAULT_DATE_DUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTodoitem() throws Exception {
        // Get the todoitem
        restTodoitemMockMvc.perform(get("/api/todoitems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodoitem() throws Exception {
        // Initialize the database
        todoitemRepository.saveAndFlush(todoitem);
        int databaseSizeBeforeUpdate = todoitemRepository.findAll().size();

        // Update the todoitem
        Todoitem updatedTodoitem = todoitemRepository.findOne(todoitem.getId());
        updatedTodoitem
            .todoName(UPDATED_TODO_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .isDone(UPDATED_IS_DONE)
            .dateDue(UPDATED_DATE_DUE);

        restTodoitemMockMvc.perform(put("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTodoitem)))
            .andExpect(status().isOk());

        // Validate the Todoitem in the database
        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeUpdate);
        Todoitem testTodoitem = todoitemList.get(todoitemList.size() - 1);
        assertThat(testTodoitem.getTodoName()).isEqualTo(UPDATED_TODO_NAME);
        assertThat(testTodoitem.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testTodoitem.isIsDone()).isEqualTo(UPDATED_IS_DONE);
        assertThat(testTodoitem.getDateDue()).isEqualTo(UPDATED_DATE_DUE);
    }

    @Test
    @Transactional
    public void updateNonExistingTodoitem() throws Exception {
        int databaseSizeBeforeUpdate = todoitemRepository.findAll().size();

        // Create the Todoitem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTodoitemMockMvc.perform(put("/api/todoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoitem)))
            .andExpect(status().isCreated());

        // Validate the Todoitem in the database
        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTodoitem() throws Exception {
        // Initialize the database
        todoitemRepository.saveAndFlush(todoitem);
        int databaseSizeBeforeDelete = todoitemRepository.findAll().size();

        // Get the todoitem
        restTodoitemMockMvc.perform(delete("/api/todoitems/{id}", todoitem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Todoitem> todoitemList = todoitemRepository.findAll();
        assertThat(todoitemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Todoitem.class);
    }
}
