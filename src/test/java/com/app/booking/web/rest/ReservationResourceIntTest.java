package com.app.booking.web.rest;

import com.app.booking.Application;
import com.app.booking.domain.Reservation;
import com.app.booking.repository.ReservationRepository;
import com.app.booking.service.ReservationService;
import com.app.booking.web.rest.dto.ReservationDTO;
import com.app.booking.web.rest.mapper.ReservationMapper;

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
 * Test class for the ReservationResource REST controller.
 *
 * @see ReservationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReservationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);
    private static final String DEFAULT_KETERANGAN = "AAAAA";
    private static final String UPDATED_KETERANGAN = "BBBBB";
    private static final String DEFAULT_CREATE_BY = "AAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    @Inject
    private ReservationRepository reservationRepository;

    @Inject
    private ReservationMapper reservationMapper;

    @Inject
    private ReservationService reservationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReservationResource reservationResource = new ReservationResource();
        ReflectionTestUtils.setField(reservationResource, "reservationService", reservationService);
        ReflectionTestUtils.setField(reservationResource, "reservationMapper", reservationMapper);
        this.restReservationMockMvc = MockMvcBuilders.standaloneSetup(reservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        reservation = new Reservation();
        reservation.setStartDate(DEFAULT_START_DATE);
        reservation.setEndDate(DEFAULT_END_DATE);
        reservation.setKeterangan(DEFAULT_KETERANGAN);
        reservation.setCreateBy(DEFAULT_CREATE_BY);
        reservation.setCreateDate(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);

        restReservationMockMvc.perform(post("/api/reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
                .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservations.get(reservations.size() - 1);
        assertThat(testReservation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testReservation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testReservation.getKeterangan()).isEqualTo(DEFAULT_KETERANGAN);
        assertThat(testReservation.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testReservation.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setStartDate(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);

        restReservationMockMvc.perform(post("/api/reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
                .andExpect(status().isBadRequest());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setEndDate(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);

        restReservationMockMvc.perform(post("/api/reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
                .andExpect(status().isBadRequest());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeteranganIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setKeterangan(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);

        restReservationMockMvc.perform(post("/api/reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
                .andExpect(status().isBadRequest());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservations
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].keterangan").value(hasItem(DEFAULT_KETERANGAN.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.keterangan").value(DEFAULT_KETERANGAN.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

		int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        reservation.setStartDate(UPDATED_START_DATE);
        reservation.setEndDate(UPDATED_END_DATE);
        reservation.setKeterangan(UPDATED_KETERANGAN);
        reservation.setCreateBy(UPDATED_CREATE_BY);
        reservation.setCreateDate(UPDATED_CREATE_DATE);
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);

        restReservationMockMvc.perform(put("/api/reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
                .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservations.get(reservations.size() - 1);
        assertThat(testReservation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testReservation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testReservation.getKeterangan()).isEqualTo(UPDATED_KETERANGAN);
        assertThat(testReservation.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testReservation.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

		int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Get the reservation
        restReservationMockMvc.perform(delete("/api/reservations/{id}", reservation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
