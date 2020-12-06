package com.app.booking.repository;

import com.app.booking.domain.Costumer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Costumer entity.
 */
public interface CostumerRepository extends JpaRepository<Costumer,Long> {

    Costumer findFirstByNamaCostumerContainingIgnoreCase(String namaCostumer);
}
