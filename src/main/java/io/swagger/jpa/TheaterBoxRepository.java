package io.swagger.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.model.TheaterBox;

//@Repository
public interface TheaterBoxRepository extends JpaRepository<TheaterBox, Long> {

    TheaterBox findByBoxNumber(Integer boxNumber);
    Boolean existsByBoxNumber(Integer boxNumber);
}
