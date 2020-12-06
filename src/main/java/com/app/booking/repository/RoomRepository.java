package com.app.booking.repository;

import com.app.booking.domain.Room;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Room entity.
 */
public interface RoomRepository extends JpaRepository<Room,Long> {

    List<Room> findAllBynamaRoomContainingIgnoreCase(String namaRoom);

    Room findFirstByNamaRoomContainingIgnoreCase(String namaRoom);
}
