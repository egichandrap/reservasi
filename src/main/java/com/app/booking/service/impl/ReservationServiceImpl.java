package com.app.booking.service.impl;

import com.app.booking.repository.RoomRepository;
import com.app.booking.security.SecurityUtils;
import com.app.booking.service.ReservationService;
import com.app.booking.domain.Reservation;
import com.app.booking.repository.ReservationRepository;
import com.app.booking.service.util.Validation;
import com.app.booking.web.rest.dto.ReservationDTO;
import com.app.booking.web.rest.mapper.ReservationMapper;
import com.app.booking.web.rest.mapper.RoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Implementation for managing Reservation.
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Inject
    private ReservationRepository reservationRepository;

    @Inject
    private ReservationMapper reservationMapper;

    @Inject
    private RoomRepository roomRepository;

    @Inject
    private RoomMapper roomMapper;

    /**
     * Save a reservation.
     * @return the persisted entity
     */
    public ReservationDTO save(ReservationDTO reservationDTO) {
        log.debug("Request to save Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.reservationDTOToReservation(reservationDTO);

        List<Reservation> sameName = reservationRepository.findAllByCostumerId(reservation.getCostumer().getId());
        List<Reservation> sameRoom = reservationRepository.findAllByRoomId(reservation.getRoom().getId());

        Validation validation = new Validation(reservation);


        if (sameName.size()>0){
            validation.checkSameCustomerAndDate(sameName);
            if (validation.hasError()){
                throw new IllegalArgumentException(validation.getError());
            }
        }

        if (sameRoom.size()>0){
            validation.checkSameRoomAndDate(sameRoom, validation.isSkipRoom());
            if (validation.hasError()){
                throw new IllegalArgumentException(validation.getError());
            }
        }
        //
        if(reservation.getId() == null) {
            reservation.setStatus("PENDING");
        }
        reservation.setCreateDate(ZonedDateTime.now());
        reservation.setCreateBy(SecurityUtils.getCurrentUserLogin());


        reservation = reservationRepository.save(reservation);
        ReservationDTO result = reservationMapper.reservationToReservationDTO(reservation);

        System.out.println("DataReservation :" +reservation);
        System.out.println("DataReservationDTO :" + result);
        log.debug("DataReservation :" +reservation);
        log.debug("DataReservationDTO :" + result);
        return result;
    }

    /**
     *  get all the reservations.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findAll(Pageable pageable) {
        log.debug("Request to get all Reservations");
        Page<Reservation> result = reservationRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one reservation by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ReservationDTO findOne(Long id) {
        log.debug("Request to get Reservation : {}", id);
        Reservation reservation = reservationRepository.findOne(id);
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        return reservationDTO;
    }

    /**
     *  delete the  reservation by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.delete(id);
    }

    @Override
    public Page<Reservation> findPageByKeyword(
        String kolomPencarian,
        String kataPencarian,
        Pageable pageable) {
        switch (kolomPencarian) {
            case "Customers":
                return reservationRepository.findAllByCostumerContainingIgnoreCase(kataPencarian.trim(), pageable);
            case "Rooms":
                return reservationRepository.findAllByRoomContainingIgnoreCase(kataPencarian.trim(), pageable);
            default:
                return findAll(pageable);
        }
    }
}
