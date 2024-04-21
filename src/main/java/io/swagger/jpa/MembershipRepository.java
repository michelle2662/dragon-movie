package io.swagger.jpa;

import io.swagger.model.Showtime;
import io.swagger.model.TheaterBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

}
