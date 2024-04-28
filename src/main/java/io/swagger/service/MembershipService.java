package io.swagger.service;

import io.swagger.jpa.MembershipRepository;
import io.swagger.model.Membership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class MembershipService implements UserDetailsService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Membership membership = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new User(membership.getEmail(), membership.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(membership.getRole())));
    }

    public Long getMemberIdByEmail(String email) {
        return membershipRepository.findByEmail(email)
        .map(Membership::getId)
        .orElse(null);
    }
}