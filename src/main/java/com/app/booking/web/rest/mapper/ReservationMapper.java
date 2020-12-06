package com.app.booking.web.rest.mapper;

import com.app.booking.domain.*;
import com.app.booking.web.rest.dto.ReservationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reservation and its DTO ReservationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReservationMapper {

    @Mapping(source = "costumer.id", target = "costumerId")
    @Mapping(source = "costumer.namaCostumer", target = "namaCostumer")
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.namaRoom", target = "namaRoom")
    ReservationDTO reservationToReservationDTO(Reservation reservation);

    @Mapping(source = "costumerId", target = "costumer")
    @Mapping(source = "roomId", target = "room")
    @Mapping(source = "status", target = "status")
    Reservation reservationDTOToReservation(ReservationDTO reservationDTO);

    default Costumer costumerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Costumer costumer = new Costumer();
        costumer.setId(id);
        return costumer;
    }

    default Room roomFromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }
}
