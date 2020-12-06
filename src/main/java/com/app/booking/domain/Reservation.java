package com.app.booking.domain;

import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @NotNull
    @Size(max = 500)
    @Column(name = "keterangan", length = 500, nullable = false)
    private String keterangan;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "costumer_id")
    private Costumer costumer;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull
    @Size(max = 10)
    @Column(name = "status", length = 10, nullable = false)
    private String status;

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {        this.keterangan = keterangan;    }


    public String getCreateBy() {      return createBy;    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Costumer getCostumer() {
        return costumer;
    }

    public void setCostumer(Costumer costumer) {
        this.costumer = costumer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation reservation = (Reservation) o;
        if(reservation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reservation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
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
