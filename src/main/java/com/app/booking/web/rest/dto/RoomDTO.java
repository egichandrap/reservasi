package com.app.booking.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Room entity.
 */
public class RoomDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String namaRoom;


    @NotNull
    @Size(max = 50)
    private String tipeRoom;


    @Size(max = 5)
    private String kapasitasRoom;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNamaRoom() {
        return namaRoom;
    }

    public void setNamaRoom(String namaRoom) {
        this.namaRoom = namaRoom;
    }
    public String getTipeRoom() {
        return tipeRoom;
    }

    public void setTipeRoom(String tipeRoom) {
        this.tipeRoom = tipeRoom;
    }
    public String getKapasitasRoom() {
        return kapasitasRoom;
    }

    public void setKapasitasRoom(String kapasitasRoom) {
        this.kapasitasRoom = kapasitasRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomDTO roomDTO = (RoomDTO) o;

        if ( ! Objects.equals(id, roomDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
            "id=" + id +
            ", namaRoom='" + namaRoom + "'" +
            ", tipeRoom='" + tipeRoom + "'" +
            ", kapasitasRoom='" + kapasitasRoom + "'" +
            '}';
    }
}
