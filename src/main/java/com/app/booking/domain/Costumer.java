package com.app.booking.domain;

import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Costumer.
 */
@Entity
@Table(name = "costumer")
public class Costumer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_costumer", length = 50, nullable = false)
    private String namaCostumer;
    
    @Column(name = "create_date")
    private ZonedDateTime createDate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaCostumer() {
        return namaCostumer;
    }
    
    public void setNamaCostumer(String namaCostumer) {
        this.namaCostumer = namaCostumer;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Costumer costumer = (Costumer) o;
        if(costumer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, costumer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Costumer{" +
            "id=" + id +
            ", namaCostumer='" + namaCostumer + "'" +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
