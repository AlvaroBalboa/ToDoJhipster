package com.theironyard.novauc.repository;

import com.theironyard.novauc.domain.Citizen;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Citizen entity.
 */
@SuppressWarnings("unused")
public interface CitizenRepository extends JpaRepository<Citizen,Long> {

}
