package io.swagger.configuration;

import io.swagger.security.JwtAuthenticationFilter;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.MembershipService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
       // new AntPathRequestMatcher("/movies/**"),
        new AntPathRequestMatcher("/membership"),
        new AntPathRequestMatcher("/theater_boxes/**"),
        //new AntPathRequestMatcher("/showtimes/**"),
        new AntPathRequestMatcher("/auth/**")
    );

    private static final RequestMatcher MEMBER_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/reservations/**"),
        new AntPathRequestMatcher("/reports/summary", HttpMethod.GET.name())
    );

    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/movies/**", HttpMethod.POST.name()),
        new AntPathRequestMatcher("/movies/*", HttpMethod.PUT.name()),
        new AntPathRequestMatcher("/movies/*", HttpMethod.DELETE.name()),
        new AntPathRequestMatcher("/theater_boxes", HttpMethod.POST.name()),
        new AntPathRequestMatcher("/showtimes", HttpMethod.POST.name()),
        new AntPathRequestMatcher("/showtimes/*", HttpMethod.PUT.name()),
        new AntPathRequestMatcher("/showtimes/*", HttpMethod.DELETE.name()),
        new AntPathRequestMatcher("/reservations/**"),
        new AntPathRequestMatcher("/reports/summary", HttpMethod.GET.name())
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
            .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
            .requestMatchers(MEMBER_URLS).hasRole("MEMBER")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            })
            .authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            });
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