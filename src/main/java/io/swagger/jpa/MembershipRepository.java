package io.swagger.jpa;

import io.swagger.model.Showtime;
import io.swagger.model.TheaterBox;
import io.swagger.model.Membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Long> findIdByEmail(String email);
}