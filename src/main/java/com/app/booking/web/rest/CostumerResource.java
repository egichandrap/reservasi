package com.app.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.app.booking.domain.Costumer;
import com.app.booking.service.CostumerService;
import com.app.booking.web.rest.util.HeaderUtil;
import com.app.booking.web.rest.util.PaginationUtil;
import com.app.booking.web.rest.dto.CostumerDTO;
import com.app.booking.web.rest.mapper.CostumerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Costumer.
 */
@RestController
@RequestMapping("/api")
public class CostumerResource {

    private final Logger log = LoggerFactory.getLogger(CostumerResource.class);
        
    @Inject
    private CostumerService costumerService;
    
    @Inject
    private CostumerMapper costumerMapper;
    
    /**
     * POST  /costumers -> Create a new costumer.
     */
    @RequestMapping(value = "/costumers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CostumerDTO> createCostumer(@Valid @RequestBody CostumerDTO costumerDTO) throws URISyntaxException {
        log.debug("REST request to save Costumer : {}", costumerDTO);
        if (costumerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("costumer", "idexists", "A new costumer cannot already have an ID")).body(null);
        }
        CostumerDTO result = costumerService.save(costumerDTO);
        return ResponseEntity.created(new URI("/api/costumers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("costumer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /costumers -> Updates an existing costumer.
     */
    @RequestMapping(value = "/costumers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CostumerDTO> updateCostumer(@Valid @RequestBody CostumerDTO costumerDTO) throws URISyntaxException {
        log.debug("REST request to update Costumer : {}", costumerDTO);
        if (costumerDTO.getId() == null) {
            return createCostumer(costumerDTO);
        }
        CostumerDTO result = costumerService.save(costumerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("costumer", costumerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /costumers -> get all the costumers.
     */
    @RequestMapping(value = "/costumers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CostumerDTO>> getAllCostumers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Costumers");
        Page<Costumer> page = costumerService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/costumers");
        return new ResponseEntity<>(page.getContent().stream()
            .map(costumerMapper::costumerToCostumerDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /costumers/:id -> get the "id" costumer.
     */
    @RequestMapping(value = "/costumers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CostumerDTO> getCostumer(@PathVariable Long id) {
        log.debug("REST request to get Costumer : {}", id);
        CostumerDTO costumerDTO = costumerService.findOne(id);
        return Optional.ofNullable(costumerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /costumers/:id -> delete the "id" costumer.
     */
    @RequestMapping(value = "/costumers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCostumer(@PathVariable Long id) {
        log.debug("REST request to delete Costumer : {}", id);
        costumerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("costumer", id.toString())).build();
    }
}
