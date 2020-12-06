package com.app.booking.repository;

import com.app.booking.domain.Reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Reservation entity.
 */
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findAllByRoomId(Long id);

    List<Reservation> findAllByCostumerId(Long id);

    Page<Reservation> findAllByRoomContainingIgnoreCase(String trim, Pageable pageable);

    Page<Reservation> findAllByCostumerContainingIgnoreCase(String trim, Pageable pageable);
}
