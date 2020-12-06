package com.app.booking.service;

import com.app.booking.domain.Reservation;
import com.app.booking.web.rest.dto.ReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Reservation.
 */
public interface ReservationService {

    /**
     * Save a reservation.
     * @return the persisted entity
     */
    public ReservationDTO save(ReservationDTO reservationDTO);

    /**
     *  get all the reservations.
     *  @return the list of entities
     */
    public Page<Reservation> findAll(Pageable pageable);

    /**
     *  get the "id" reservation.
     *  @return the entity
     */
    public ReservationDTO findOne(Long id);

    /**
     *  delete the "id" reservation.
     */
    public void delete(Long id);

    Page<Reservation> findPageByKeyword(String kolomPencarian, String kataPencarian, Pageable pageable);

}
