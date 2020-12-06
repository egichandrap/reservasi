package com.app.booking.web.rest.mapper;

import com.app.booking.domain.*;
import com.app.booking.web.rest.dto.CostumerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Costumer and its DTO CostumerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CostumerMapper {

    CostumerDTO costumerToCostumerDTO(Costumer costumer);

    Costumer costumerDTOToCostumer(CostumerDTO costumerDTO);
}
