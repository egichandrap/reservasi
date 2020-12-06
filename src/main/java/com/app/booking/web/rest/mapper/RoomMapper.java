package com.app.booking.web.rest.mapper;

import com.app.booking.domain.*;
import com.app.booking.web.rest.dto.RoomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Room and its DTO RoomDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoomMapper {

    RoomDTO roomToRoomDTO(Room room);

    Room roomDTOToRoom(RoomDTO roomDTO);
}
