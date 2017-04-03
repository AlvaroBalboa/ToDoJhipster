package com.theironyard.novauc.repository;

import com.theironyard.novauc.domain.Todoitem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Todoitem entity.
 */
@SuppressWarnings("unused")
public interface TodoitemRepository extends JpaRepository<Todoitem,Long> {

}
