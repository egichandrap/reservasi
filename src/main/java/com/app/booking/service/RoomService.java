package com.app.booking.service;

import com.app.booking.domain.Room;
import com.app.booking.web.rest.dto.RoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Room.
 */
public interface RoomService {

    /**
     * Save a room.
     * @return the persisted entity
     */
    public RoomDTO save(RoomDTO roomDTO);

    /**
     *  get all the rooms.
     *  @return the list of entities
     */
    public Page<Room> findAll(Pageable pageable);

    /**
     *  get the "id" room.
     *  @return the entity
     */
    public RoomDTO findOne(Long id);

    /**
     *  delete the "id" room.
     */
    public void delete(Long id);
}
