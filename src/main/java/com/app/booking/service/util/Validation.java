package com.app.booking.service.util;

import com.app.booking.domain.Costumer;
import com.app.booking.domain.Reservation;
import com.app.booking.domain.Room;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Validation {

    @Inject
    private Reservation reservation;
    private Long reservationId;
    private Long costumerId;
    private Long roomId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, dd MM yyy, hh:mm a");

    @Inject
    private Room room;
    private Long rmId;
    private String namaRoom;

    @Inject
    private Costumer costumer;
    private Long cstId;
    private String namCostumer;


    private String error = null;
    private Boolean isError = false;
    private Boolean skip = false;


    public Validation(Reservation reservation){
        this.reservation = reservation;
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.costumerId = reservation.getCostumer().getId();
        this.roomId = reservation.getRoom().getId();
        this.reservationId = reservation.getId();
    }

    public Validation(Room room) {
        this.rmId = room.getId();
        this.namaRoom = room.getNamaRoom();
    }

    public Validation(Costumer costumer) {
        this.cstId = costumer.getId();
        this.namCostumer = costumer.getNamaCostumer();

    }

    public void checkSameCustomerAndDate(List<Reservation> reservationSameName){
        for (Reservation itr : reservationSameName){
            if (reservationId != null && itr.getId().equals(reservationId)){
                this.skip = true;
                return;
            }

            ZonedDateTime itrStart = itr.getStartDate();
            ZonedDateTime itrEnd = itr.getEndDate();

            boolean startInRange = Date.isValidDateRange(this.startDate, itrStart, itrEnd);
            boolean endInRange = Date.isValidDateRange(this.endDate, itrStart, itrEnd);


            if (startInRange || endInRange){
                this.isError = true;
                this.error = "The Costumer Already has Reservation at diffferent room, try to use different time";
                break;
            }
        }
    }

    public void checkSameRoomAndDate(List<Reservation> reservationSameRoom, boolean skipSameId) {
        for (Reservation itr : reservationSameRoom) {
            if (skipSameId && itr.getRoom().getId().equals(roomId)) {
                continue;
            }
            ZonedDateTime itrStart = itr.getStartDate();
            ZonedDateTime itrEnd = itr.getEndDate();

            boolean startInRange = Date.isValidDateRange(this.startDate, itrStart, itrEnd);
            boolean endInRange = Date.isValidDateRange(this.endDate, itrStart, itrEnd);

            if (startInRange || endInRange) {
                this.isError = true;
                this.error = "\"" + itr.getRoom().getNamaRoom() + "\" sudah dipesan: \nStart: "
                    + itrStart.format(dateFormat) + "\nEnd: " + itrEnd.format(dateFormat);
                break;
            }
        }
    }

//    public void checkSameRoom(List<Room> roomSameRoom) {
//        for (Room itr : roomSameRoom) {
//            if (rmId != null && itr.getId().equals(rmId)){
//                this.skip = true;
//                return;
//            }
//
//            String itrRoomOld = itr.getNamaRoom();
//            String itrRoomNew = itr.getNamaRoom();
//
//            boolean roomOld = RoomValidation.isValidRoom(this.namaRoom, itrRoomOld, itrRoomNew);
//            boolean roomNew = RoomValidation.isValidRoom(this.namaRoom, itrRoomOld, itrRoomNew);
//
//            if (roomOld || roomNew){
//                this.isError = true;
//                this.error = "Room available, please use another room name.";
//                break;
//
//            }
//        }
//    }


    public String getError() {
        return this.error;
    }

    public String getErrorRoom() {
        return "Room available, please use another room name.";
    }

    public String getErrorCostumer() {
        return "Name Customer available, please use another name customer.";
    }

    public boolean hasError() {
        final boolean get = this.isError;
        this.isError = false;
        return get;
    }

    public boolean isSkipRoom() {
        return this.skip;
    }

}
