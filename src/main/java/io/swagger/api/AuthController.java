package io.swagger.api;

import io.swagger.jpa.MembershipRepository;
import io.swagger.model.JwtResponse;
import io.swagger.model.LoginRequest;
import io.swagger.model.Membership;
import io.swagger.model.MembershipRequestBody;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/membership")
    public ResponseEntity<?> createMembership(@Valid @RequestBody MembershipRequestBody membershipRequestBody) {
        if (membershipRepository.existsByEmail(membershipRequestBody.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        Membership membership = new Membership();
        membership.setFirstName(membershipRequestBody.getFirstName());
        membership.setLastName(membershipRequestBody.getLastName());
        membership.setEmail(membershipRequestBody.getEmail());
        membership.setPassword(passwordEncoder.encode(membershipRequestBody.getPassword()));
        membership.setRole("ROLE_MEMBER");

        membershipRepository.save(membership);

        return ResponseEntity.ok("Membership registered successfully!");
    }
}