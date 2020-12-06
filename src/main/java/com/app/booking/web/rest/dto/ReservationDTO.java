package com.app.booking.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Reservation entity.
 */
public class ReservationDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startDate;


    @NotNull
    private ZonedDateTime endDate;


    @NotNull
    @Size(max = 500)
    private String keterangan;
    @Size(max = 10)
    private String status;
    private String createBy;
    private ZonedDateTime createDate;
    private Long costumerId;
    private String namaCostumer;
    private Long roomId;
    private String namaRoom;

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }
    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }
    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(Long costumerId) {
        this.costumerId = costumerId;
    }

    public String getNamaCostumer() {
        return namaCostumer;
    }

    public void setNamaCostumer(String namaCostumer) {
        this.namaCostumer = namaCostumer;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getNamaRoom() {
        return namaRoom;
    }

    public void setNamaRoom(String namaRoom) {
        this.namaRoom = namaRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReservationDTO reservationDTO = (ReservationDTO) o;

        if ( ! Objects.equals(id, reservationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + id +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", keterangan='" + keterangan + "'" +
            ", createBy='" + createBy + "'" +
            ", createDate='" + createDate + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
