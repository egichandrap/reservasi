package com.app.booking.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_room", length = 50, nullable = false)
    private String namaRoom;
    
    @NotNull
    @Size(max = 50)
    @Column(name = "tipe_room", length = 50, nullable = false)
    private String tipeRoom;
    
    @Size(max = 5)
    @Column(name = "kapasitas_room", length = 5)
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
        Room room = (Room) o;
        if(room.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Room{" +
            "id=" + id +
            ", namaRoom='" + namaRoom + "'" +
            ", tipeRoom='" + tipeRoom + "'" +
            ", kapasitasRoom='" + kapasitasRoom + "'" +
            '}';
    }
}
