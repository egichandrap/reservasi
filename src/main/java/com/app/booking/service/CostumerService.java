package com.app.booking.service;

import com.app.booking.domain.Costumer;
import com.app.booking.web.rest.dto.CostumerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Costumer.
 */
public interface CostumerService {

    /**
     * Save a costumer.
     * @return the persisted entity
     */
    public CostumerDTO save(CostumerDTO costumerDTO);

    /**
     *  get all the costumers.
     *  @return the list of entities
     */
    public Page<Costumer> findAll(Pageable pageable);

    /**
     *  get the "id" costumer.
     *  @return the entity
     */
    public CostumerDTO findOne(Long id);

    /**
     *  delete the "id" costumer.
     */
    public void delete(Long id);
}
