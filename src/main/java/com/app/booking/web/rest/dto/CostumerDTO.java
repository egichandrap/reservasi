package com.app.booking.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Costumer entity.
 */
public class CostumerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String namaCostumer;


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

        CostumerDTO costumerDTO = (CostumerDTO) o;

        if ( ! Objects.equals(id, costumerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CostumerDTO{" +
            "id=" + id +
            ", namaCostumer='" + namaCostumer + "'" +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
