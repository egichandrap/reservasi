package com.app.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.app.booking.domain.Reservation;
import com.app.booking.service.ReservationService;
import com.app.booking.web.rest.util.HeaderUtil;
import com.app.booking.web.rest.util.PaginationUtil;
import com.app.booking.web.rest.dto.ReservationDTO;
import com.app.booking.web.rest.mapper.ReservationMapper;
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
import javax.transaction.Status;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Reservation.
 */
@RestController
@RequestMapping("/api")
public class ReservationResource {

    private final Logger log = LoggerFactory.getLogger(ReservationResource.class);

    @Inject
    private ReservationService reservationService;

    @Inject
    private ReservationMapper reservationMapper;

    /**
     * POST  /reservations -> Create a new reservation.
     */
    @RequestMapping(value = "/reservations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) throws URISyntaxException {
        log.debug("REST request to save Reservation : {}", reservationDTO);
        if (reservationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reservation", "idexists", "A new reservation cannot already have an ID")).body(null);
        }
        ReservationDTO result = reservationService.save(reservationDTO);

        return ResponseEntity.created(new URI("/api/reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reservation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reservations -> Updates an existing reservation.
     */
    @RequestMapping(value = "/reservations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReservationDTO> updateReservation(@Valid @RequestBody ReservationDTO reservationDTO) throws URISyntaxException {
        log.debug("REST request to update Reservation : {}", reservationDTO);
        if (reservationDTO.getId() == null) {
            return createReservation(reservationDTO);
        }
        ReservationDTO result = reservationService.save(reservationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reservation", reservationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reservations -> get all the reservations.
     */
    @RequestMapping(value = "/reservations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReservationDTO>> getAllReservations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Reservations");
        Page<Reservation> page = reservationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reservations");
        return new ResponseEntity<>(page.getContent().stream()
            .map(reservationMapper::reservationToReservationDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**re
     * GET  /reservations/:id -> get the "id" reservation.
     */
    @RequestMapping(value = "/reservations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long id) {
        log.debug("REST request to get Reservation : {}", id);
        ReservationDTO reservationDTO = reservationService.findOne(id);
        return Optional.ofNullable(reservationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reservations/:id -> delete the "id" reservation.
     */
    @RequestMapping(value = "/reservations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        log.debug("REST request to delete Reservation : {}", id);
        reservationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reservation", id.toString())).build();
    }


    /**
     * GET  /reservations -> get page by keyword the reservations.
     */
    @RequestMapping(value = "/reservations/getPageByKeyword",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReservationDTO>> getPageByKeyword(
        @RequestParam("kolomPencarian") String kolomPencarian,
        @RequestParam("kataPencarian") String kataPencarian,
        Pageable pageable
    ) throws URISyntaxException {
        log.debug("REST AREA GET PAGE BY KEYWORD");
        Page<Reservation> page = reservationService.findPageByKeyword(
            kolomPencarian,
            kataPencarian,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reservations");
        return new ResponseEntity<>(
            page.getContent()
                .stream()
                .map(reservationMapper::reservationToReservationDTO)
                .collect(Collectors.toCollection(LinkedList::new)),
            headers,
            HttpStatus.OK
        );
    }


}
