package com.app.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.app.booking.domain.Room;
import com.app.booking.service.RoomService;
import com.app.booking.web.rest.util.HeaderUtil;
import com.app.booking.web.rest.util.PaginationUtil;
import com.app.booking.web.rest.dto.RoomDTO;
import com.app.booking.web.rest.mapper.RoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Room.
 */
@RestController
@RequestMapping("/api")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);
        
    @Inject
    private RoomService roomService;
    
    @Inject
    private RoomMapper roomMapper;
    
    /**
     * POST  /rooms -> Create a new room.
     */
    @RequestMapping(value = "/rooms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to save Room : {}", roomDTO);
        if (roomDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("room", "idexists", "A new room cannot already have an ID")).body(null);
        }
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity.created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("room", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rooms -> Updates an existing room.
     */
    @RequestMapping(value = "/rooms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoomDTO> updateRoom(@Valid @RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to update Room : {}", roomDTO);
        if (roomDTO.getId() == null) {
            return createRoom(roomDTO);
        }
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("room", roomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rooms -> get all the rooms.
     */
    @RequestMapping(value = "/rooms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RoomDTO>> getAllRooms(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Rooms");
        Page<Room> page = roomService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rooms");
        return new ResponseEntity<>(page.getContent().stream()
            .map(roomMapper::roomToRoomDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /rooms/:id -> get the "id" room.
     */
    @RequestMapping(value = "/rooms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        RoomDTO roomDTO = roomService.findOne(id);
        return Optional.ofNullable(roomDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rooms/:id -> delete the "id" room.
     */
    @RequestMapping(value = "/rooms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("room", id.toString())).build();
    }
}
