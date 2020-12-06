package com.app.booking.web.rest;

import com.app.booking.Application;
import com.app.booking.domain.Room;
import com.app.booking.repository.RoomRepository;
import com.app.booking.service.RoomService;
import com.app.booking.web.rest.dto.RoomDTO;
import com.app.booking.web.rest.mapper.RoomMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RoomResource REST controller.
 *
 * @see RoomResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RoomResourceIntTest {

    private static final String DEFAULT_NAMA_ROOM = "AAAAA";
    private static final String UPDATED_NAMA_ROOM = "BBBBB";
    private static final String DEFAULT_TIPE_ROOM = "AAAAA";
    private static final String UPDATED_TIPE_ROOM = "BBBBB";
    private static final String DEFAULT_KAPASITAS_ROOM = "AAAAA";
    private static final String UPDATED_KAPASITAS_ROOM = "BBBBB";

    @Inject
    private RoomRepository roomRepository;

    @Inject
    private RoomMapper roomMapper;

    @Inject
    private RoomService roomService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRoomMockMvc;

    private Room room;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoomResource roomResource = new RoomResource();
        ReflectionTestUtils.setField(roomResource, "roomService", roomService);
        ReflectionTestUtils.setField(roomResource, "roomMapper", roomMapper);
        this.restRoomMockMvc = MockMvcBuilders.standaloneSetup(roomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        room = new Room();
        room.setNamaRoom(DEFAULT_NAMA_ROOM);
        room.setTipeRoom(DEFAULT_TIPE_ROOM);
        room.setKapasitasRoom(DEFAULT_KAPASITAS_ROOM);
    }

    @Test
    @Transactional
    public void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // Create the Room
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);

        restRoomMockMvc.perform(post("/api/rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
                .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = rooms.get(rooms.size() - 1);
        assertThat(testRoom.getNamaRoom()).isEqualTo(DEFAULT_NAMA_ROOM);
        assertThat(testRoom.getTipeRoom()).isEqualTo(DEFAULT_TIPE_ROOM);
        assertThat(testRoom.getKapasitasRoom()).isEqualTo(DEFAULT_KAPASITAS_ROOM);
    }

    @Test
    @Transactional
    public void checkNamaRoomIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setNamaRoom(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);

        restRoomMockMvc.perform(post("/api/rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
                .andExpect(status().isBadRequest());

        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipeRoomIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setTipeRoom(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);

        restRoomMockMvc.perform(post("/api/rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
                .andExpect(status().isBadRequest());

        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the rooms
        restRoomMockMvc.perform(get("/api/rooms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
                .andExpect(jsonPath("$.[*].namaRoom").value(hasItem(DEFAULT_NAMA_ROOM.toString())))
                .andExpect(jsonPath("$.[*].tipeRoom").value(hasItem(DEFAULT_TIPE_ROOM.toString())))
                .andExpect(jsonPath("$.[*].kapasitasRoom").value(hasItem(DEFAULT_KAPASITAS_ROOM.toString())));
    }

    @Test
    @Transactional
    public void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.namaRoom").value(DEFAULT_NAMA_ROOM.toString()))
            .andExpect(jsonPath("$.tipeRoom").value(DEFAULT_TIPE_ROOM.toString()))
            .andExpect(jsonPath("$.kapasitasRoom").value(DEFAULT_KAPASITAS_ROOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

		int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        room.setNamaRoom(UPDATED_NAMA_ROOM);
        room.setTipeRoom(UPDATED_TIPE_ROOM);
        room.setKapasitasRoom(UPDATED_KAPASITAS_ROOM);
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);

        restRoomMockMvc.perform(put("/api/rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
                .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = rooms.get(rooms.size() - 1);
        assertThat(testRoom.getNamaRoom()).isEqualTo(UPDATED_NAMA_ROOM);
        assertThat(testRoom.getTipeRoom()).isEqualTo(UPDATED_TIPE_ROOM);
        assertThat(testRoom.getKapasitasRoom()).isEqualTo(UPDATED_KAPASITAS_ROOM);
    }

    @Test
    @Transactional
    public void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

		int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Get the room
        restRoomMockMvc.perform(delete("/api/rooms/{id}", room.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeDelete - 1);
    }
}
