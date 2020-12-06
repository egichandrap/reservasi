package com.app.booking.web.rest;

import com.app.booking.Application;
import com.app.booking.domain.Costumer;
import com.app.booking.repository.CostumerRepository;
import com.app.booking.service.CostumerService;
import com.app.booking.web.rest.dto.CostumerDTO;
import com.app.booking.web.rest.mapper.CostumerMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CostumerResource REST controller.
 *
 * @see CostumerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CostumerResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAMA_COSTUMER = "AAAAA";
    private static final String UPDATED_NAMA_COSTUMER = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    @Inject
    private CostumerRepository costumerRepository;

    @Inject
    private CostumerMapper costumerMapper;

    @Inject
    private CostumerService costumerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCostumerMockMvc;

    private Costumer costumer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CostumerResource costumerResource = new CostumerResource();
        ReflectionTestUtils.setField(costumerResource, "costumerService", costumerService);
        ReflectionTestUtils.setField(costumerResource, "costumerMapper", costumerMapper);
        this.restCostumerMockMvc = MockMvcBuilders.standaloneSetup(costumerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        costumer = new Costumer();
        costumer.setNamaCostumer(DEFAULT_NAMA_COSTUMER);
        costumer.setCreateDate(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createCostumer() throws Exception {
        int databaseSizeBeforeCreate = costumerRepository.findAll().size();

        // Create the Costumer
        CostumerDTO costumerDTO = costumerMapper.costumerToCostumerDTO(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
                .andExpect(status().isCreated());

        // Validate the Costumer in the database
        List<Costumer> costumers = costumerRepository.findAll();
        assertThat(costumers).hasSize(databaseSizeBeforeCreate + 1);
        Costumer testCostumer = costumers.get(costumers.size() - 1);
        assertThat(testCostumer.getNamaCostumer()).isEqualTo(DEFAULT_NAMA_COSTUMER);
        assertThat(testCostumer.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void checkNamaCostumerIsRequired() throws Exception {
        int databaseSizeBeforeTest = costumerRepository.findAll().size();
        // set the field null
        costumer.setNamaCostumer(null);

        // Create the Costumer, which fails.
        CostumerDTO costumerDTO = costumerMapper.costumerToCostumerDTO(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
                .andExpect(status().isBadRequest());

        List<Costumer> costumers = costumerRepository.findAll();
        assertThat(costumers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCostumers() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

        // Get all the costumers
        restCostumerMockMvc.perform(get("/api/costumers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(costumer.getId().intValue())))
                .andExpect(jsonPath("$.[*].namaCostumer").value(hasItem(DEFAULT_NAMA_COSTUMER.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

        // Get the costumer
        restCostumerMockMvc.perform(get("/api/costumers/{id}", costumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(costumer.getId().intValue()))
            .andExpect(jsonPath("$.namaCostumer").value(DEFAULT_NAMA_COSTUMER.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCostumer() throws Exception {
        // Get the costumer
        restCostumerMockMvc.perform(get("/api/costumers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

		int databaseSizeBeforeUpdate = costumerRepository.findAll().size();

        // Update the costumer
        costumer.setNamaCostumer(UPDATED_NAMA_COSTUMER);
        costumer.setCreateDate(UPDATED_CREATE_DATE);
        CostumerDTO costumerDTO = costumerMapper.costumerToCostumerDTO(costumer);

        restCostumerMockMvc.perform(put("/api/costumers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
                .andExpect(status().isOk());

        // Validate the Costumer in the database
        List<Costumer> costumers = costumerRepository.findAll();
        assertThat(costumers).hasSize(databaseSizeBeforeUpdate);
        Costumer testCostumer = costumers.get(costumers.size() - 1);
        assertThat(testCostumer.getNamaCostumer()).isEqualTo(UPDATED_NAMA_COSTUMER);
        assertThat(testCostumer.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void deleteCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

		int databaseSizeBeforeDelete = costumerRepository.findAll().size();

        // Get the costumer
        restCostumerMockMvc.perform(delete("/api/costumers/{id}", costumer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Costumer> costumers = costumerRepository.findAll();
        assertThat(costumers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
