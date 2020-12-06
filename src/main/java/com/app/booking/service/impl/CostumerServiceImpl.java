package com.app.booking.service.impl;

import com.app.booking.service.CostumerService;
import com.app.booking.domain.Costumer;
import com.app.booking.repository.CostumerRepository;
import com.app.booking.service.util.Validation;
import com.app.booking.web.rest.dto.CostumerDTO;
import com.app.booking.web.rest.mapper.CostumerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Costumer.
 */
@Service
@Transactional
public class CostumerServiceImpl implements CostumerService{

    private final Logger log = LoggerFactory.getLogger(CostumerServiceImpl.class);

    @Inject
    private CostumerRepository costumerRepository;

    @Inject
    private CostumerMapper costumerMapper;

    /**
     * Save a costumer.
     * @return the persisted entity
     */
    public CostumerDTO save(CostumerDTO costumerDTO) {
        log.debug("Request to save Costumer : {}", costumerDTO);
        Costumer costumer = costumerMapper.costumerDTOToCostumer(costumerDTO);

        Costumer costumersame = costumerRepository.findFirstByNamaCostumerContainingIgnoreCase(costumer.getNamaCostumer());

        Validation validation = new Validation((costumer));
        if (costumersame!=null){
            throw new IllegalArgumentException(validation.getErrorCostumer());
        }
        costumer.setCreateDate(ZonedDateTime.now());
        costumer = costumerRepository.save(costumer);
        CostumerDTO result = costumerMapper.costumerToCostumerDTO(costumer);
        return result;
    }

    /**
     *  get all the costumers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Costumer> findAll(Pageable pageable) {
        log.debug("Request to get all Costumers");
        Page<Costumer> result = costumerRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one costumer by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CostumerDTO findOne(Long id) {
        log.debug("Request to get Costumer : {}", id);
        Costumer costumer = costumerRepository.findOne(id);
        CostumerDTO costumerDTO = costumerMapper.costumerToCostumerDTO(costumer);
        return costumerDTO;
    }

    /**
     *  delete the  costumer by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Costumer : {}", id);
        costumerRepository.delete(id);
    }
}
