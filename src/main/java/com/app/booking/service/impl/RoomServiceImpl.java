package com.app.booking.service.impl;

import com.app.booking.security.SecurityUtils;
import com.app.booking.service.RoomService;
import com.app.booking.domain.Room;
import com.app.booking.repository.RoomRepository;
import com.app.booking.service.util.Validation;
import com.app.booking.web.rest.dto.RoomDTO;
import com.app.booking.web.rest.mapper.RoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Room.
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService{

    private final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Inject
    private RoomRepository roomRepository;

    @Inject
    private RoomMapper roomMapper;

    /**
     * Save a room.
     * @return the persisted entity
     */
    public RoomDTO save(RoomDTO roomDTO) {
        log.debug("Request to save Room : {}", roomDTO);
        Room room = roomMapper.roomDTOToRoom(roomDTO);

//        List<Room> roomSameRoom = roomRepository.findAllBynamaRoomContainingIgnoreCase(room.getNamaRoom());

        Room roomsame = roomRepository.findFirstByNamaRoomContainingIgnoreCase(room.getNamaRoom());

        Validation validation = new Validation((room));
        if (roomsame!=null) {
            throw new IllegalArgumentException(validation.getErrorRoom());
        }
//
//        if (roomSameRoom.size()>0){
//            validation.checkSameRoom(roomSameRoom);
//            if (validation.hasError()){
//                throw new IllegalArgumentException(validation.getError());
//            }
//        }
        System.out.println("USER = "+SecurityUtils.getCurrentUserLogin());

//        room.setCreateBy(SecurityUtils.getCurrentUserLogin());
//        room.setCreateDate(ZonedDateTime.now());
        room = roomRepository.save(room);
        RoomDTO result = roomMapper.roomToRoomDTO(room);
        return result;
    }

    /**
     *  get all the rooms.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Room> findAll(Pageable pageable) {
        log.debug("Request to get all Rooms");
        Page<Room> result = roomRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one room by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RoomDTO findOne(Long id) {
        log.debug("Request to get Room : {}", id);
        Room room = roomRepository.findOne(id);
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);
        return roomDTO;
    }

    /**
     *  delete the  room by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Room : {}", id);
        roomRepository.delete(id);
    }
}
