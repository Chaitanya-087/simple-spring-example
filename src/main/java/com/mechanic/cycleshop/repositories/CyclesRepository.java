package com.mechanic.cycleshop.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mechanic.cycleshop.entities.Cycle;
import java.util.List;


public interface CyclesRepository extends CrudRepository<Cycle, Integer> {
    @Query(value = "select * from cycles where quantity > ?1", nativeQuery = true)
    List<Cycle> findAllCyclesByQuantityGreaterThan(int qty);

}
