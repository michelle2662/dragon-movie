package io.swagger.configuration;

import io.swagger.security.JwtAuthenticationFilter;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/movies/**"),
        new AntPathRequestMatcher("/membership"),
        new AntPathRequestMatcher("/theater_boxes/**"),
        new AntPathRequestMatcher("/showtimes/**")
    );

    private static final RequestMatcher MEMBER_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/reservation/**")
    );

    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/movies", "POST"),
        new AntPathRequestMatcher("/movies/*", "PUT"),
        new AntPathRequestMatcher("/movies/*", "DELETE"),
        new AntPathRequestMatcher("/theater_boxes", "POST"),
        new AntPathRequestMatcher("/showtimes", "POST"),
        new AntPathRequestMatcher("/showtimes/*", "PUT"),
        new AntPathRequestMatcher("/showtimes/*", "DELETE")
    );

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MembershipService membershipService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(MEMBER_URLS).hasRole("MEMBER")
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(membershipService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}