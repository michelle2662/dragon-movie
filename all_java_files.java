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
        new AntPathRequestMatcher("/reservation/**"),
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
        new AntPathRequestMatcher("/reservation/**"),
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
}package io.swagger.configuration;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.threeten.bp.LocalDate;

import io.swagger.jpa.MembershipRepository;
import io.swagger.jpa.MovieRepository;
import io.swagger.model.Membership;
import io.swagger.model.Movie;


@Configuration
public class DatabaseInitialization {
	
	private static final Logger log = LoggerFactory.getLogger(DatabaseInitialization.class);

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initDatabase(MovieRepository movieRepository, MembershipRepository membershipRepository) {
		log.info("Preloading data");
		
		return args -> {
			// id = 1
			movieRepository.save(new Movie("Dune: Part Two", "Denis Villeneuve", "Action", "PG-13", "2h46m",
					LocalDate.parse("2024-02-06"), new BigDecimal(8.7), true, false));
			
			// id = 1
			membershipRepository.save(new Membership(1l, "Jane", "Doe", "jane.doe@email.com", passwordEncoder.encode("test123"), "ROLE_ADMIN"));
		};
	}

}
package io.swagger.configuration;

import org.springframework.core.convert.converter.Converter;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<String, LocalDate> {
    private final DateTimeFormatter formatter;

    public LocalDateConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate convert(String source) {
        if(source == null || source.isEmpty()) {
            return null;
        }
        return LocalDate.parse(source, this.formatter);
    }
}
package io.swagger.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@Configuration
public class SwaggerUiConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
    }
}
package io.swagger.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.datatype.threetenbp.DecimalUtils;
import com.fasterxml.jackson.datatype.threetenbp.deser.ThreeTenDateTimeDeserializerBase;
import com.fasterxml.jackson.datatype.threetenbp.function.BiFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.Function;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Deserializer for ThreeTen temporal {@link Instant}s, {@link OffsetDateTime}, and {@link ZonedDateTime}s.
 * Adapted from the jackson threetenbp InstantDeserializer to add support for deserializing rfc822 format.
 *
 * @author Nick Williams
 */
public class CustomInstantDeserializer<T extends Temporal>
    extends ThreeTenDateTimeDeserializerBase<T> {
  private static final long serialVersionUID = 1L;

  public static final CustomInstantDeserializer<Instant> INSTANT = new CustomInstantDeserializer<Instant>(
      Instant.class, DateTimeFormatter.ISO_INSTANT,
      new Function<TemporalAccessor, Instant>() {
        @Override
        public Instant apply(TemporalAccessor temporalAccessor) {
          return Instant.from(temporalAccessor);
        }
      },
      new Function<FromIntegerArguments, Instant>() {
        @Override
        public Instant apply(FromIntegerArguments a) {
          return Instant.ofEpochMilli(a.value);
        }
      },
      new Function<FromDecimalArguments, Instant>() {
        @Override
        public Instant apply(FromDecimalArguments a) {
          return Instant.ofEpochSecond(a.integer, a.fraction);
        }
      },
      null
  );

  public static final CustomInstantDeserializer<OffsetDateTime> OFFSET_DATE_TIME = new CustomInstantDeserializer<OffsetDateTime>(
      OffsetDateTime.class, DateTimeFormatter.ISO_OFFSET_DATE_TIME,
      new Function<TemporalAccessor, OffsetDateTime>() {
        @Override
        public OffsetDateTime apply(TemporalAccessor temporalAccessor) {
          return OffsetDateTime.from(temporalAccessor);
        }
      },
      new Function<FromIntegerArguments, OffsetDateTime>() {
        @Override
        public OffsetDateTime apply(FromIntegerArguments a) {
          return OffsetDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId);
        }
      },
      new Function<FromDecimalArguments, OffsetDateTime>() {
        @Override
        public OffsetDateTime apply(FromDecimalArguments a) {
          return OffsetDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction), a.zoneId);
        }
      },
      new BiFunction<OffsetDateTime, ZoneId, OffsetDateTime>() {
        @Override
        public OffsetDateTime apply(OffsetDateTime d, ZoneId z) {
          return d.withOffsetSameInstant(z.getRules().getOffset(d.toLocalDateTime()));
        }
      }
  );

  public static final CustomInstantDeserializer<ZonedDateTime> ZONED_DATE_TIME = new CustomInstantDeserializer<ZonedDateTime>(
      ZonedDateTime.class, DateTimeFormatter.ISO_ZONED_DATE_TIME,
      new Function<TemporalAccessor, ZonedDateTime>() {
        @Override
        public ZonedDateTime apply(TemporalAccessor temporalAccessor) {
          return ZonedDateTime.from(temporalAccessor);
        }
      },
      new Function<FromIntegerArguments, ZonedDateTime>() {
        @Override
        public ZonedDateTime apply(FromIntegerArguments a) {
          return ZonedDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId);
        }
      },
      new Function<FromDecimalArguments, ZonedDateTime>() {
        @Override
        public ZonedDateTime apply(FromDecimalArguments a) {
          return ZonedDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction), a.zoneId);
        }
      },
      new BiFunction<ZonedDateTime, ZoneId, ZonedDateTime>() {
        @Override
        public ZonedDateTime apply(ZonedDateTime zonedDateTime, ZoneId zoneId) {
          return zonedDateTime.withZoneSameInstant(zoneId);
        }
      }
  );

  protected final Function<FromIntegerArguments, T> fromMilliseconds;

  protected final Function<FromDecimalArguments, T> fromNanoseconds;

  protected final Function<TemporalAccessor, T> parsedToValue;

  protected final BiFunction<T, ZoneId, T> adjust;

  protected CustomInstantDeserializer(Class<T> supportedType,
                    DateTimeFormatter parser,
                    Function<TemporalAccessor, T> parsedToValue,
                    Function<FromIntegerArguments, T> fromMilliseconds,
                    Function<FromDecimalArguments, T> fromNanoseconds,
                    BiFunction<T, ZoneId, T> adjust) {
    super(supportedType, parser);
    this.parsedToValue = parsedToValue;
    this.fromMilliseconds = fromMilliseconds;
    this.fromNanoseconds = fromNanoseconds;
    this.adjust = adjust == null ? new BiFunction<T, ZoneId, T>() {
      @Override
      public T apply(T t, ZoneId zoneId) {
        return t;
      }
    } : adjust;
  }

  @SuppressWarnings("unchecked")
  protected CustomInstantDeserializer(CustomInstantDeserializer<T> base, DateTimeFormatter f) {
    super((Class<T>) base.handledType(), f);
    parsedToValue = base.parsedToValue;
    fromMilliseconds = base.fromMilliseconds;
    fromNanoseconds = base.fromNanoseconds;
    adjust = base.adjust;
  }

  @Override
  protected JsonDeserializer<T> withDateFormat(DateTimeFormatter dtf) {
    if (dtf == _formatter) {
      return this;
    }
    return new CustomInstantDeserializer<T>(this, dtf);
  }

  @Override
  public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    //NOTE: Timestamps contain no timezone info, and are always in configured TZ. Only
    //string values have to be adjusted to the configured TZ.
    switch (parser.getCurrentTokenId()) {
      case JsonTokenId.ID_NUMBER_FLOAT: {
        BigDecimal value = parser.getDecimalValue();
        long seconds = value.longValue();
        int nanoseconds = DecimalUtils.extractNanosecondDecimal(value, seconds);
        return fromNanoseconds.apply(new FromDecimalArguments(
            seconds, nanoseconds, getZone(context)));
      }

      case JsonTokenId.ID_NUMBER_INT: {
        long timestamp = parser.getLongValue();
        if (context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
          return this.fromNanoseconds.apply(new FromDecimalArguments(
              timestamp, 0, this.getZone(context)
          ));
        }
        return this.fromMilliseconds.apply(new FromIntegerArguments(
            timestamp, this.getZone(context)
        ));
      }

      case JsonTokenId.ID_STRING: {
        String string = parser.getText().trim();
        if (string.length() == 0) {
          return null;
        }
        if (string.endsWith("+0000")) {
          string = string.substring(0, string.length() - 5) + "Z";
        }
        T value;
        try {
          TemporalAccessor acc = _formatter.parse(string);
          value = parsedToValue.apply(acc);
          if (context.isEnabled(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)) {
            return adjust.apply(value, this.getZone(context));
          }
        } catch (DateTimeException e) {
          throw _peelDTE(e);
        }
        return value;
      }
    }
    throw context.mappingException("Expected type float, integer, or string.");
  }

  private ZoneId getZone(DeserializationContext context) {
    // Instants are always in UTC, so don't waste compute cycles
    return (_valueClass == Instant.class) ? null : DateTimeUtils.toZoneId(context.getTimeZone());
  }

  private static class FromIntegerArguments {
    public final long value;
    public final ZoneId zoneId;

    private FromIntegerArguments(long value, ZoneId zoneId) {
      this.value = value;
      this.zoneId = zoneId;
    }
  }

  private static class FromDecimalArguments {
    public final long integer;
    public final int fraction;
    public final ZoneId zoneId;

    private FromDecimalArguments(long integer, int fraction, ZoneId zoneId) {
      this.integer = integer;
      this.fraction = fraction;
      this.zoneId = zoneId;
    }
  }
}
package io.swagger.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/swagger-ui/";
    }
}
package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@Configuration
public class SwaggerDocumentationConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Dragon Movie")
                .description("Dragon Movie project")
                .termsOfService("")
                .version("1.0")
                .license(new License()
                    .name("")
                    .url("http://unlicense.org"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("")));
    }

}
package io.swagger.configuration;

import org.springframework.core.convert.converter.Converter;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
    private final DateTimeFormatter formatter;

    public LocalDateTimeConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDateTime convert(String source) {
        if(source == null || source.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(source, this.formatter);
    }
}
package io.swagger.configuration;

import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;

@Configuration
public class JacksonConfiguration {

  @Bean
  @ConditionalOnMissingBean(ThreeTenModule.class)
  ThreeTenModule threeTenModule() {
    ThreeTenModule module = new ThreeTenModule();
    module.addDeserializer(Instant.class, CustomInstantDeserializer.INSTANT);
    module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
    module.addDeserializer(ZonedDateTime.class, CustomInstantDeserializer.ZONED_DATE_TIME);
    return module;
  }
}
package io.swagger.security;

import io.swagger.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MembershipService membershipService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = membershipService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}package io.swagger.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String JWT_SECRET = "your-jwt-secret";
    private static final long JWT_EXPIRATION_MS = 86400000; // 24 hours

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}package io.swagger;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;

import java.text.FieldPosition;
import java.util.Date;


public class RFC3339DateFormat extends ISO8601DateFormat {

  private static final long serialVersionUID = 1L;

  // Same as ISO8601DateFormat but serializing milliseconds.
  @Override
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    String value = ISO8601Utils.format(date, true);
    toAppendTo.append(value);
    return toAppendTo;
  }

}package io.swagger.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.swagger.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m.title, SUM(tb.ticketPrice * r.seatsReserved) AS totalRevenue " +
       "FROM Movie m " +
       "JOIN m.showtimes s " +
       "JOIN s.reservations r " +
       "JOIN r.theaterBox tb " +
       "WHERE s.dateTime BETWEEN :startTime AND :endTime " +
       "GROUP BY m.title")
    List<Object[]> findTotalRevenueByMovie(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
package io.swagger.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.model.TheaterBox;

//@Repository
public interface TheaterBoxRepository extends JpaRepository<TheaterBox, Long> {
    TheaterBox findByBoxNumber(Integer boxNumber);

}
package io.swagger.jpa;

import io.swagger.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}package io.swagger.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.swagger.model.Movie;
import io.swagger.model.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("SELECT s.movie FROM Showtime s WHERE s.id = :showtimeId")
    List<Movie> findMoviesByShowtimeId(@Param("showtimeId") Long showtimeId);
}package io.swagger.jpa;

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
}package io.swagger;

import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateTimeConverter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class Swagger2SpringBoot implements CommandLineRunner {

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    @Configuration
    static class CustomDateConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new LocalDateConverter("yyyy-MM-dd"));
            registry.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        }
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ShowtimeRequestBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T15:29:40.414361-04:00[America/New_York]")


public class ShowtimeRequestBody   {
  @JsonProperty("date_time")
  private OffsetDateTime dateTime = null;

  @JsonProperty("movie_id")
  private Integer movieId = null;

  @JsonProperty("theater_box_id")
  private Integer theaterBoxId = null;

  public ShowtimeRequestBody dateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  /**
   * Date and time of the showtime.
   * @return dateTime
   **/
  @Schema(example = "2023-12-15T20:00Z", required = true, description = "Date and time of the showtime.")
      @NotNull

    @Valid
    public OffsetDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public ShowtimeRequestBody movieId(Integer movieId) {
    this.movieId = movieId;
    return this;
  }

  /**
   * ID of the movie associated with the showtime.
   * @return movieId
   **/
  @Schema(example = "123", required = true, description = "ID of the movie associated with the showtime.")
      @NotNull

    public Integer getMovieId() {
    return movieId;
  }

  public void setMovieId(Integer movieId) {
    this.movieId = movieId;
  }

  public ShowtimeRequestBody theaterBoxId(Integer theaterBoxId) {
    this.theaterBoxId = theaterBoxId;
    return this;
  }

  /**
   * ID of the theater box associated with the showtime.
   * @return theaterBoxId
   **/
  @Schema(example = "5", required = true, description = "ID of the theater box associated with the showtime.")
      @NotNull

    public Integer getTheaterBoxId() {
    return theaterBoxId;
  }

  public void setTheaterBoxId(Integer theaterBoxId) {
    this.theaterBoxId = theaterBoxId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowtimeRequestBody showtimeRequestBody = (ShowtimeRequestBody) o;
    return Objects.equals(this.dateTime, showtimeRequestBody.dateTime) &&
        Objects.equals(this.movieId, showtimeRequestBody.movieId) &&
        Objects.equals(this.theaterBoxId, showtimeRequestBody.theaterBoxId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateTime, movieId, theaterBoxId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowtimeRequestBody {\n");
    
    sb.append("    dateTime: ").append(toIndentedString(dateTime)).append("\n");
    sb.append("    movieId: ").append(toIndentedString(movieId)).append("\n");
    sb.append("    theaterBoxId: ").append(toIndentedString(theaterBoxId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Reservation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id = null;

	@ManyToOne
    @JoinColumn(name = "showtime_id", referencedColumnName = "id")
	private Showtime showtime = null;
	
	@ManyToOne
    @JoinColumn(name = "theater_box_id", referencedColumnName = "id")
    private TheaterBox theaterBox = null;

	@JsonProperty("seatsReserved")
	private Integer seatsReserved = null;

	@Schema(description = "Identifier for reservation")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Reservation showtimeId(Showtime showtime) {
		this.showtime = showtime;
		return this;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

	public Reservation theaterBox(TheaterBox theaterBox) {
		this.theaterBox = theaterBox;
		return this;
	}

	@Schema(description = "")
	@Valid
	public TheaterBox getTheaterBox() {
		return theaterBox;
	}

	public void setTheaterBox(TheaterBox theaterBox) {
		this.theaterBox = theaterBox;
	}

	@Schema(description = "Number of seats reserved")
	public Integer getSeatsReserved() {
		return seatsReserved;
	}

	public void setSeatsReserved(Integer seatsReserved) {
		this.seatsReserved = seatsReserved;
	}

	public Reservation seatsReserved(Integer seatsReserved) {
		this.seatsReserved = seatsReserved;
		return this;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Reservation reservation = (Reservation) o;
		return Objects.equals(this.id, reservation.id) && Objects.equals(this.showtime, reservation.showtime)
				&& Objects.equals(this.theaterBox, reservation.theaterBox) && Objects.equals(this.seatsReserved, reservation.seatsReserved);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, showtime, theaterBox, seatsReserved);
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", showtime=" + showtime+ ", theaterBoxNumber=" + theaterBox + ", seatsReserved=" + seatsReserved + "]";
	}

}
package io.swagger.model;

import java.util.Objects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Identifier of the showtime associated with the reservation
 */
@Schema(description = "Identifier of the showtime associated with the reservation")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class AllOfReservationShowtimeId   {

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllOfReservationShowtimeId {\n");
    
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
package io.swagger.model;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Showtime
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T15:29:40.414361-04:00[America/New_York]")

@Entity
public class Showtime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id = null;

  @JsonProperty("date_time")
  private OffsetDateTime dateTime = null;

  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id")
  private Movie movie;

  @JsonProperty("theater_box_id")
  private Integer theaterBoxId = null;

  @OneToMany(mappedBy = "showtime")
  private Set<Reservation> reservations;

  public Showtime id(Long id) {
    this.id = id;
    return this;
  }

    /**
   * Unique identifier of the showtime.
   * @return id
   **/
  @Schema(example = "104", required = true, description = "Unique identifier of the showtime.")
      @NotNull

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Showtime dateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  /**
   * Date and time of the showtime.
   * @return dateTime
   **/
  @Schema(example = "2023-12-15T20:00Z", required = true, description = "Date and time of the showtime.")
  @NotNull
  @Valid
  public OffsetDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public Movie getMovie() {
    return movie;
  }

  public void setMovie(Movie movie) {
    this.movie = movie;
  }

  public Showtime theaterBoxId(Integer theaterBoxId) {
    this.theaterBoxId = theaterBoxId;
    return this;
  }

  /**
   * ID of the theater box associated with the showtime.
   * @return theaterBoxId
   **/
  @Schema(example = "5", required = true, description = "ID of the theater box associated with the showtime.")
  @NotNull
  public Integer getTheaterBoxId() {
    return theaterBoxId;
  }

  public void setTheaterBoxId(Integer theaterBoxId) {
    this.theaterBoxId = theaterBoxId;
  }

  public Set<Reservation> getReservations() {
    return reservations;
  }

  public void setReservations(Set<Reservation> reservations) {
    this.reservations = reservations;
  }

  public Showtime reservations(Set<Reservation> reservations) {
    this.reservations = reservations;
    return this;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Showtime showtime = (Showtime) o;
    return Objects.equals(this.id, showtime.id) &&
        Objects.equals(this.dateTime, showtime.dateTime) &&
        Objects.equals(this.movie, showtime.movie) &&
        Objects.equals(this.theaterBoxId, showtime.theaterBoxId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateTime, movie, theaterBoxId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Showtime {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dateTime: ").append(toIndentedString(dateTime)).append("\n");
    sb.append("    movie: ").append(toIndentedString(movie)).append("\n");
    sb.append("    theaterBoxId: ").append(toIndentedString(theaterBoxId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReservationReserveBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class ReservationReserveBody   {
  @JsonProperty("seats")
  private Integer seats = null;

  public ReservationReserveBody seats(Integer seats) {
    this.seats = seats;
    return this;
  }

  /**
   * Number of seats to reserve.
   * minimum: 1
   * @return seats
   **/
  @Schema(required = true, description = "Number of seats to reserve.")
      @NotNull

  @Min(1)  public Integer getSeats() {
    return seats;
  }

  public void setSeats(Integer seats) {
    this.seats = seats;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationReserveBody reservationReserveBody = (ReservationReserveBody) o;
    return Objects.equals(this.seats, reservationReserveBody.seats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReservationReserveBody {\n");
    
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReservationModifyBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class ReservationModifyBody   {
  @JsonProperty("seats")
  private Integer seats = null;

  public ReservationModifyBody seats(Integer seats) {
    this.seats = seats;
    return this;
  }

  /**
   * New number of seats for the reservation.
   * minimum: 1
   * @return seats
   **/
  @Schema(required = true, description = "New number of seats for the reservation.")
      @NotNull

  @Min(1)  public Integer getSeats() {
    return seats;
  }

  public void setSeats(Integer seats) {
    this.seats = seats;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationModifyBody reservationModifyBody = (ReservationModifyBody) o;
    return Objects.equals(this.seats, reservationModifyBody.seats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReservationModifyBody {\n");
    
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TheaterBox
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class TheaterBox {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
	
	@Column(unique = true)
    private Integer boxNumber = null;

	@JsonProperty("total_seats")
	private Integer totalSeats = null;

	@JsonProperty("reserved_seats")
	private Integer reservedSeats = null;

	@JsonProperty("ticket_price")
	private Float ticketPrice = null;

	public TheaterBox boxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TheaterBox id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Number of the theater box.
	 * 
	 * @return boxNumber
	 **/
	@Schema(description = "Number of the theater box.")

	public Integer getBoxNumber() {
		return boxNumber;
	}

	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}

	public TheaterBox totalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
		return this;
	}

	/**
	 * Total number of seats in the theater box.
	 * 
	 * @return totalSeats
	 **/
	@Schema(description = "Total number of seats in the theater box.")

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}

	public TheaterBox reservedSeats(Integer reservedSeats) {
		this.reservedSeats = reservedSeats;
		return this;
	}

	/**
	 * Number of reserved seats in the theater box.
	 * 
	 * @return reservedSeats
	 **/
	@Schema(description = "Number of reserved seats in the theater box.")

	public Integer getReservedSeats() {
		return reservedSeats;
	}

	public void setReservedSeats(Integer reservedSeats) {
		this.reservedSeats = reservedSeats;
	}

	public TheaterBox ticketPrice(Float ticketPrice) {
		this.ticketPrice = ticketPrice;
		return this;
	}

	/**
	 * Price of a ticket for the theater box.
	 * 
	 * @return ticketPrice
	 **/
	@Schema(description = "Price of a ticket for the theater box.")

	public Float getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Float ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TheaterBox theaterBox = (TheaterBox) o;
		return Objects.equals(this.boxNumber, theaterBox.boxNumber)
				&& Objects.equals(this.totalSeats, theaterBox.totalSeats)
				&& Objects.equals(this.reservedSeats, theaterBox.reservedSeats)
				&& Objects.equals(this.ticketPrice, theaterBox.ticketPrice);
	}

	@Override
	public int hashCode() {
		return Objects.hash(boxNumber, totalSeats, reservedSeats, ticketPrice);
	}

	@Override
	public String toString() {
		return "TheaterBox [boxNumber=" + boxNumber + ", totalSeats=" + totalSeats + ", reservedSeats=" + reservedSeats
				+ ", ticketPrice=" + ticketPrice + "]";
	}
	
}
package io.swagger.model;

public class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * MovieRequestBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T13:14:07.895681+01:00[Europe/London]")

public class MovieRequestBody {
	@JsonProperty("title")
	private String title = null;

	@JsonProperty("director")
	private String director = null;

	@JsonProperty("genre")
	private String genre = null;

	@JsonProperty("rating")
	private String rating = null;

	@JsonProperty("length")
	private String length = null;

	@JsonProperty("releaseDate")
	private LocalDate releaseDate = null;

	@JsonProperty("reviewScore")
	private BigDecimal reviewScore = null;

	@JsonProperty("currentlyPlaying")
	private Boolean currentlyPlaying = null;

	@JsonProperty("upcomingRelease")
	private Boolean upcomingRelease = null;

	public MovieRequestBody title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Get title
	 * 
	 * @return title
	 **/
	@Schema(example = "Oppenheimer", description = "")
	@NotNull
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MovieRequestBody director(String director) {
		this.director = director;
		return this;
	}

	/**
	 * Get director
	 * 
	 * @return director
	 **/
	@Schema(example = "Christopher Nolan", description = "")
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public MovieRequestBody genre(String genre) {
		this.genre = genre;
		return this;
	}

	/**
	 * Get genre
	 * 
	 * @return genre
	 **/
	@Schema(example = "Thriller", description = "")
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public MovieRequestBody rating(String rating) {
		this.rating = rating;
		return this;
	}

	/**
	 * Get rating
	 * 
	 * @return rating
	 **/
	@Schema(example = "R", description = "")
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public MovieRequestBody length(String length) {
		this.length = length;
		return this;
	}

	/**
	 * Get length
	 * 
	 * @return length
	 **/
	@Schema(example = "3h5m", description = "")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public MovieRequestBody releaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
		return this;
	}

	/**
	 * Get releaseDate
	 * 
	 * @return releaseDate
	 **/
	@Schema(example = "Fri Jul 21 01:00:00 BST 2023", description = "")
	@Valid
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public MovieRequestBody reviewScore(BigDecimal reviewScore) {
		this.reviewScore = reviewScore;
		return this;
	}

	/**
	 * Get reviewScore
	 * 
	 * @return reviewScore
	 **/
	@Schema(example = "8.9", description = "")
	@Valid
	public BigDecimal getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(BigDecimal reviewScore) {
		this.reviewScore = reviewScore;
	}

	public MovieRequestBody currentlyPlaying(Boolean currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
		return this;
	}

	/**
	 * Get currentlyPlaying
	 * 
	 * @return currentlyPlaying
	 **/
	@Schema(example = "true", description = "")
	public Boolean isCurrentlyPlaying() {
		return currentlyPlaying;
	}

	public void setCurrentlyPlaying(Boolean currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
	}

	public MovieRequestBody upcomingRelease(Boolean upcomingRelease) {
		this.upcomingRelease = upcomingRelease;
		return this;
	}

	/**
	 * Get upcomingRelease
	 * 
	 * @return upcomingRelease
	 **/
	@Schema(example = "false", description = "")
	public Boolean isUpcomingRelease() {
		return upcomingRelease;
	}

	public void setUpcomingRelease(Boolean upcomingRelease) {
		this.upcomingRelease = upcomingRelease;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MovieRequestBody movieRequestBody = (MovieRequestBody) o;
		return Objects.equals(this.title, movieRequestBody.title)
				&& Objects.equals(this.director, movieRequestBody.director)
				&& Objects.equals(this.genre, movieRequestBody.genre)
				&& Objects.equals(this.rating, movieRequestBody.rating)
				&& Objects.equals(this.length, movieRequestBody.length)
				&& Objects.equals(this.releaseDate, movieRequestBody.releaseDate)
				&& Objects.equals(this.reviewScore, movieRequestBody.reviewScore)
				&& Objects.equals(this.currentlyPlaying, movieRequestBody.currentlyPlaying)
				&& Objects.equals(this.upcomingRelease, movieRequestBody.upcomingRelease);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, director, genre, rating, length, releaseDate, reviewScore, currentlyPlaying,
				upcomingRelease);
	}

	@Override
	public String toString() {
		return "MovieRequestBody [title=" + title + ", director=" + director + ", genre=" + genre + ", rating=" + rating
				+ ", length=" + length + ", releaseDate=" + releaseDate + ", reviewScore=" + reviewScore
				+ ", currentlyPlaying=" + currentlyPlaying + ", upcomingRelease=" + upcomingRelease + "]";
	}

}
package io.swagger.model;

public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}package io.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


/**
 * Membership
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Membership {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@JsonProperty("firstName")
	private String firstName = null;

	@JsonProperty("lastName")
	private String lastName = null;

	@JsonProperty("email")
	@Column(unique = true)
	private String email = null;

	@JsonIgnore // This annotation prevents the password from being serialized
	private String password;

	@JsonProperty("role")
    private String role;

	public Membership() {

	}

	public Membership(Long id, String firstName, String lastName, String email, String password, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Schema(example = "John", description = "")
	@NotNull
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Schema(example = "Smith", description = "")
	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Schema(example = "john.smith@email.com", description = "")
	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Schema(example = "password", description = "")
	@NotNull
	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Schema(example = "MEMBER", description = "")
	@NotNull
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Membership membership = (Membership) o;
		return Objects.equals(this.firstName, membership.firstName)
				&& Objects.equals(this.lastName, membership.lastName) && Objects.equals(this.email, membership.email) && Objects.equals(this.password, membership.password) &&
                Objects.equals(this.role, membership.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, email, password, role);
		}

	@Override
	public String toString() {
		return "Membership [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", role='" + role 
				+ "]";
	}


}
package io.swagger.model;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Movie
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Movie {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonProperty("title")
	private String title = null;

	@JsonProperty("director")
	private String director = null;

	@JsonProperty("genre")
	private String genre = null;

	@JsonProperty("rating")
	private String rating = null;

	@JsonProperty("length")
	private String length = null;

	@JsonProperty("releaseDate")
	private LocalDate releaseDate = null;

	@JsonProperty("reviewScore")
	private BigDecimal reviewScore = null;

	@JsonProperty("currentlyPlaying")
	private Boolean currentlyPlaying = null;

	@JsonProperty("upcomingRelease")
	private Boolean upcomingRelease = null;

	@OneToMany(mappedBy = "movie")
	private Set<Showtime> showtimes;

	public Movie() {
		
	}

	public Movie(Long id, String title, String director, String genre, String rating, String length, LocalDate releaseDate,
			BigDecimal reviewScore, Boolean currentlyPlaying, Boolean upcomingRelease) {
		super();
		this.id = id;
		this.title = title;
		this.director = director;
		this.genre = genre;
		this.rating = rating;
		this.length = length;
		this.releaseDate = releaseDate;
		this.reviewScore = reviewScore;
		this.currentlyPlaying = currentlyPlaying;
		this.upcomingRelease = upcomingRelease;
	}
	
	public Movie(String title, String director, String genre, String rating, String length, LocalDate releaseDate,
			BigDecimal reviewScore, Boolean currentlyPlaying, Boolean upcomingRelease) {
		super();
		this.title = title;
		this.director = director;
		this.genre = genre;
		this.rating = rating;
		this.length = length;
		this.releaseDate = releaseDate;
		this.reviewScore = reviewScore;
		this.currentlyPlaying = currentlyPlaying;
		this.upcomingRelease = upcomingRelease;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Schema(example = "Oppenheimer", description = "")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Schema(example = "Christopher Nolan", description = "")
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	@Schema(example = "Thriller", description = "")
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Schema(example = "R", description = "")
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Schema(example = "3h5m", description = "")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@Schema(example = "2023-07-21", description = "")
	@Valid
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Schema(example = "8.9", description = "")
	@Valid
	@Min(0)
	@Max(10)
	public BigDecimal getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(BigDecimal reviewScore) {
		this.reviewScore = reviewScore;
	}

	@Schema(example = "true", description = "")
	public Boolean isCurrentlyPlaying() {
		return currentlyPlaying;
	}

	public void setCurrentlyPlaying(Boolean currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
	}

	@Schema(example = "false", description = "")
	public Boolean isUpcomingRelease() {
		return upcomingRelease;
	}

	public void setUpcomingRelease(Boolean upcomingRelease) {
		this.upcomingRelease = upcomingRelease;
	}

	public Set<Showtime> getShowtimes() {
		return showtimes;
	}

	public void setShowtimes(Set<Showtime> showtimes) {
		this.showtimes = showtimes;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Movie movie = (Movie) o;
		return Objects.equals(this.title, movie.title) && Objects.equals(this.director, movie.director)
				&& Objects.equals(this.genre, movie.genre) && Objects.equals(this.rating, movie.rating)
				&& Objects.equals(this.length, movie.length) && Objects.equals(this.releaseDate, movie.releaseDate)
				&& Objects.equals(this.reviewScore, movie.reviewScore)
				&& Objects.equals(this.currentlyPlaying, movie.currentlyPlaying)
				&& Objects.equals(this.upcomingRelease, movie.upcomingRelease);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, director, genre, rating, length, releaseDate, reviewScore, currentlyPlaying,
				upcomingRelease);
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", director=" + director + ", genre=" + genre + ", rating="
				+ rating + ", length=" + length + ", releaseDate=" + releaseDate + ", reviewScore=" + reviewScore
				+ ", currentlyPlaying=" + currentlyPlaying + ", upcomingRelease=" + upcomingRelease + "]";
	}

}
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * MembershipRequestBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T13:14:07.895681+01:00[Europe/London]")

public class MembershipRequestBody {
	@JsonProperty("firstName")
	private String firstName = null;

	@JsonProperty("lastName")
	private String lastName = null;

	@JsonProperty("email")
	private String email = null;

	@JsonProperty("password")
	private String password = null;

	public MembershipRequestBody firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * Get firstName
	 * 
	 * @return firstName
	 **/
	@Schema(example = "John", required = true, description = "")
	@NotNull
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public MembershipRequestBody lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * Get lastName
	 * 
	 * @return lastName
	 **/
	@Schema(example = "Smith", required = true, description = "")
	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public MembershipRequestBody email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Get email
	 * 
	 * @return email
	 **/
	@Schema(example = "john.smith@email.com", required = true, description = "")
	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get password
	 * 
	 * @return password
	 **/
	@Schema(example = "password", required = true, description = "")
	@NotNull
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MembershipRequestBody password(String password) {
		this.password = password;
		return this;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MembershipRequestBody membershipRequestBody = (MembershipRequestBody) o;
		return Objects.equals(this.firstName, membershipRequestBody.firstName)
				&& Objects.equals(this.lastName, membershipRequestBody.lastName)
				&& Objects.equals(this.email, membershipRequestBody.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, email);
	}

	@Override
	public String toString() {
		return "MembershipRequestBody [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}
package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
public class ApiException extends Exception {
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.54).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import java.math.BigDecimal;
import io.swagger.model.Movie;
import io.swagger.model.MovieRequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-25T21:59:48.447063-04:00[America/New_York]")
@Validated
public interface MoviesApi {

    @Operation(summary = "Get all movies", description = "Get details of all movies", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A list of movies", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Movie.class)))) })
    @RequestMapping(value = "/movies",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Movie>> moviesGet();


    @Operation(summary = "Delete a movie", description = "Allows an admin to delete a movie", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successfully deleted movie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action.") })
    @RequestMapping(value = "/movies/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Movie> moviesIdDelete(@Parameter(in = ParameterIn.PATH, description = "the id of the movie to delete", required=true, schema=@Schema()) @PathVariable("id") Integer id
);


    @Operation(summary = "Retrieve details about a movie", description = "Returns details about a specific movie", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successfully retrieved movie details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))) })
    @RequestMapping(value = "/movies/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Movie> moviesIdGet(@Parameter(in = ParameterIn.PATH, description = "the id of the movie to get details about", required=true, schema=@Schema()) @PathVariable("id") Integer id
);


    @Operation(summary = "Update a movie", description = "Allows an admin to update a movie", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successfully updated movie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action.") })
    @RequestMapping(value = "/movies/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Movie> moviesIdPut(@Parameter(in = ParameterIn.PATH, description = "the id of the movie to update", required=true, schema=@Schema()) @PathVariable("id") Integer id
, @Parameter(in = ParameterIn.QUERY, description = "the title of the movie to update" ,schema=@Schema()) @Valid @RequestParam(value = "title", required = false) String title
, @Parameter(in = ParameterIn.QUERY, description = "the updated director" ,schema=@Schema()) @Valid @RequestParam(value = "director", required = false) String director
, @Parameter(in = ParameterIn.QUERY, description = "the updated genre" ,schema=@Schema()) @Valid @RequestParam(value = "genre", required = false) String genre
, @Parameter(in = ParameterIn.QUERY, description = "the updated rating" ,schema=@Schema()) @Valid @RequestParam(value = "rating", required = false) String rating
, @Parameter(in = ParameterIn.QUERY, description = "the updated length" ,schema=@Schema()) @Valid @RequestParam(value = "length", required = false) String length
, @Parameter(in = ParameterIn.QUERY, description = "the updated review score" ,schema=@Schema()) @Valid @RequestParam(value = "reviewScore", required = false) BigDecimal reviewScore
, @Parameter(in = ParameterIn.QUERY, description = "the updated release date" ,schema=@Schema()) @Valid @RequestParam(value = "releaseDate", required = false) String releaseDate
, @Parameter(in = ParameterIn.QUERY, description = "the updated value for if the movie is currently playing" ,schema=@Schema()) @Valid @RequestParam(value = "currentlyPlaying", required = false) Boolean currentlyPlaying
, @Parameter(in = ParameterIn.QUERY, description = "the updated value for if the move is an upcoming release" ,schema=@Schema()) @Valid @RequestParam(value = "upcomingRelease", required = false) Boolean upcomingRelease
);


    @Operation(summary = "Create a movie", description = "Allows an admin to create a movie", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "successfully created movie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action.") })
    @RequestMapping(value = "/movies",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Movie> moviesPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody MovieRequestBody body
);

}

/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.54).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Movie;
import io.swagger.model.Showtime;
import io.swagger.model.ShowtimeRequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.hibernate.internal.util.type.PrimitiveWrapperHelper.LongDescriptor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-25T21:59:48.447063-04:00[America/New_York]")
@Validated
public interface ShowtimesApi {

    @Operation(summary = "Get all showtimes", description = "Returns a list of all showtimes.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A list of showtimes.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Showtime.class)))) })
    @RequestMapping(value = "/showtimes",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Showtime>> showtimesGet();


    @Operation(summary = "Create a new showtime", description = "Allows an admin to create a new showtime.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Showtime successfully created."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Admin access token is invalid."),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action.") })
    @RequestMapping(value = "/showtimes",
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> showtimesPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ShowtimeRequestBody body
);


    @Operation(summary = "Delete a showtime", description = "Allows an admin to delete a showtime.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Showtime successfully deleted."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Admin access token is invalid."),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action."),
        
        @ApiResponse(responseCode = "404", description = "Not found. Showtime not found.") })
    @RequestMapping(value = "/showtimes/{showtime_id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> showtimesShowtimeIdDelete(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to delete.", required=true, schema=@Schema()) @PathVariable("showtime_id") Long showtimeId
);


    @Operation(summary = "Get details about a specific showtime", description = "Returns details about a specific showtime.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Details about the showtime.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Showtime.class))) })
    @RequestMapping(value = "/showtimes/{showtime_id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Showtime> showtimesShowtimeIdGet(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve details for.", required=true, schema=@Schema()) @PathVariable("showtime_id") Long showtimeId
);


    @Operation(summary = "Get movies for a specific showtime", description = "Returns a list of movies playing at a specific showtime.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A list of movies playing at the showtime.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Movie.class)))) })
    @RequestMapping(value = "/showtimes/{showtime_id}/movies",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Movie>> showtimesShowtimeIdMoviesGet(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve movies for.", required=true, schema=@Schema()) @PathVariable("showtime_id") Long showtimeId
);


    @Operation(summary = "Update a showtime", description = "Allows an admin to update a showtime.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Showtime successfully updated."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Admin access token is invalid."),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action."),
        
        @ApiResponse(responseCode = "404", description = "Not found. Showtime not found.") })
    @RequestMapping(value = "/showtimes/{showtime_id}",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> showtimesShowtimeIdPut(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to update.", required=true, schema=@Schema()) @PathVariable("showtime_id") Long showtimeId
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ShowtimeRequestBody body
);

}

package io.swagger.api;

import javax.xml.bind.annotation.XmlTransient;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@javax.xml.bind.annotation.XmlRootElement
public class ApiResponseMessage {
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int OK = 4;
    public static final int TOO_BUSY = 5;

    int code;
    String type;
    String message;

    public ApiResponseMessage(){}

    public ApiResponseMessage(int code, String message){
        this.code = code;
        switch(code){
        case ERROR:
            setType("error");
            break;
        case WARNING:
            setType("warning");
            break;
        case INFO:
            setType("info");
            break;
        case OK:
            setType("ok");
            break;
        case TOO_BUSY:
            setType("too busy");
            break;
        default:
            setType("unknown");
            break;
        }
        this.message = message;
    }

    @XmlTransient
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.54).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Reservation;
import io.swagger.model.ReservationModifyBody;
import io.swagger.model.ReservationReserveBody;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-25T21:59:48.447063-04:00[America/New_York]")
@Validated
public interface ReservationApi {

    @Operation(summary = "Cancel reservation for a given showtime", description = "Allows a member to cancel their reservation for a given showtime.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Reservation successfully canceled.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Member access token is invalid."),
        
        @ApiResponse(responseCode = "404", description = "Not found. Theater box, showtime, or reservation not found."),
        
        @ApiResponse(responseCode = "409", description = "Conflict. Showtime has already occurred.") })
    @RequestMapping(value = "/reservation/cancel",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Reservation> reservationCancelDelete(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box_id", required = true) Long theaterBoxId
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to cancel." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) Long reservationId
);


    @Operation(summary = "Modify reservation for a given showtime", description = "Allows a member to modify their reservation for a given showtime.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Reservation successfully modified.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid number of seats requested or seats not available."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Member access token is invalid."),
        
        @ApiResponse(responseCode = "404", description = "Not found. Theater box, showtime, or reservation not found."),
        
        @ApiResponse(responseCode = "409", description = "Conflict. Requested number of seats not available for the given showtime.") })
    @RequestMapping(value = "/reservation/modify",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Reservation> reservationModifyPut(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Number of the theater box." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box_id", required = true) Long theaterBoxId
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to modify." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) Long reservationId
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationModifyBody body
);


    @Operation(summary = "Reserve seats for a given showtime", description = "Allows a member to reserve one or more seats for a given showtime.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Seats successfully reserved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid number of seats requested or seats not available."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Member access token is invalid."),
        
        @ApiResponse(responseCode = "404", description = "Not found. Theater box or showtime not found."),
        
        @ApiResponse(responseCode = "409", description = "Conflict. Requested number of seats not available.") })
    @RequestMapping(value = "/reservation/reserve",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Reservation> reservationReservePost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box_id", required = true) Long theaterBoxId
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationReserveBody body
);

}

package io.swagger.api;

import io.swagger.jpa.MembershipRepository;
import io.swagger.model.Membership;
import io.swagger.model.MembershipRequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T09:54:05.273201+01:00[Europe/London]")
@RestController
public class MembershipApiController implements MembershipApi {

	private static final Logger log = LoggerFactory.getLogger(MembershipApiController.class);
	
	@Autowired
	private MembershipRepository membershipRepository;
	
	private static final String API_PATH = "apis/MORGANMAZER/dragon/1.0/";

	@Autowired
	public MembershipApiController() {
	}

	public ResponseEntity<Membership> membershipIdGet(
			@Parameter(in = ParameterIn.PATH, description = "the id of the membership", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
		log.info("GET /membership id " + id);
		
		// retrieve membership
		Optional<Membership> optionalMembership = membershipRepository.findById((long) id);
		
		if (optionalMembership.isPresent()) {
			Membership membership = optionalMembership.get();
			return ResponseEntity.ok().body(membership);
		} else {
			return ResponseEntity.ok().build();
		}
	}

	public ResponseEntity<Membership> membershipPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody MembershipRequestBody body) {
		log.info("POST /membership " + body.toString());
		
		// convert MembershipRequestBody to Membership
		Membership membership = new Membership();
		membership.setFirstName(body.getFirstName());
		membership.setLastName(body.getLastName());
		membership.setEmail(body.getEmail());
		
		// save the membership
		membershipRepository.save(membership);
		
		// build URI for newly created membership
		String host = System.getProperty("host", "localhost");
		String port = System.getProperty("port", "8080");
		String baseUrl = "http://{host}:{port}/" + API_PATH + "membership/";
		
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).host(host).port(port).path("{id}").build(membership.getId());
		
		return ResponseEntity.created(uri).body(membership); 
	}

}
package io.swagger.api;

import io.swagger.jpa.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummaryReport(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endTime) {
        
        List<Object[]> results = movieRepository.findTotalRevenueByMovie(startTime.toLocalDateTime(), endTime.toLocalDateTime());

        Map<String, Object> response = new HashMap<>();
        for (Object[] result : results) {
            String movieTitle = (String) result[0];
            Double totalRevenue = (Double) result[1];
            response.put(movieTitle, totalRevenue);
        }

        return ResponseEntity.ok(response);
    }
}
package io.swagger.api;

import io.swagger.jpa.MovieRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.model.Movie;
import io.swagger.model.Showtime;
import io.swagger.model.ShowtimeRequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ShowtimesApiController implements ShowtimesApi {

    private static final Logger log = LoggerFactory.getLogger(ShowtimesApiController.class);

    private final HttpServletRequest request;

    private static final String API_PATH = "apis/MORGANMAZER/dragon/1.0/showtimes/";

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    public ShowtimesApiController(HttpServletRequest request) {
        this.request = request;
    }

    public ResponseEntity<List<Showtime>> showtimesGet() {
        log.info("GET /showtimes");

        List<Showtime> showtimes = showtimeRepository.findAll();
        return ResponseEntity.ok().body(showtimes);
    }

    public ResponseEntity<Void> showtimesPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ShowtimeRequestBody body) {
        log.info("POST /showtimes");

        Optional<Movie> movie = movieRepository.findById(Long.valueOf(body.getMovieId()));
        if (movie.isPresent()) {
            Showtime showtime = new Showtime();
            showtime.setDateTime(body.getDateTime());
            showtime.setMovie(movie.get());
            showtime.setTheaterBoxId(body.getTheaterBoxId());

            Showtime createdShowtime = showtimeRepository.save(showtime);
            URI location = UriComponentsBuilder.fromPath("apis/MORGANMAZER/dragon/1.0/showtimes/" + createdShowtime.getId()).build().toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> showtimesShowtimeIdDelete(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to delete.", required=true, schema=@Schema()) @PathVariable("showtime_id") Long showtimeId
) {
        log.info("DELETE /showtimes/{}", showtimeId);

        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        if (optionalShowtime.isPresent()) {
            showtimeRepository.delete(optionalShowtime.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    public ResponseEntity<Showtime> showtimesShowtimeIdGet(
            @Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve details for.", required = true, schema = @Schema()) @PathVariable("showtime_id") Long showtimeId) {

        log.info("GET /showtimes/{}", showtimeId);

        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        return optionalShowtime.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Movie>> showtimesShowtimeIdMoviesGet(
            @Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve movies for.", required = true, schema = @Schema()) @PathVariable("showtime_id") Long showtimeId) {
                log.info("GET /showtimes/{}/movies", showtimeId);

                List<Movie> movies = showtimeRepository.findMoviesByShowtimeId(showtimeId);
                return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Void> showtimesShowtimeIdPut(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to update.", required=true, schema=@Schema()) @PathVariable("showtime_id") Long showtimeId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ShowtimeRequestBody body
) {
        log.info("PUT /showtimes/{}", showtimeId);

        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        Optional<Movie> movie = movieRepository.findById(Long.valueOf(body.getMovieId()));
        if (optionalShowtime.isPresent()) {
            Showtime showtime = optionalShowtime.get();
            showtime.setDateTime(body.getDateTime());
            showtime.setMovie(movie.get());
            showtime.setTheaterBoxId(body.getTheaterBoxId());

            showtimeRepository.save(showtime);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}package io.swagger.api;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import io.swagger.jpa.MovieRepository;
import io.swagger.model.Movie;
import io.swagger.model.MovieRequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.threeten.bp.LocalDate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T09:54:05.273201+01:00[Europe/London]")
@RestController
public class MoviesApiController implements MoviesApi {

	private static final Logger log = LoggerFactory.getLogger(MoviesApiController.class);

	private final HttpServletRequest request;
	
	private static final String API_PATH = "apis/MORGANMAZER/dragon/1.0/";
	
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	public MoviesApiController(HttpServletRequest request) {
		this.request = request;
	}
	
    public ResponseEntity<List<Movie>> moviesGet() {
        log.info("GET /movies");
        
        List<Movie> movies = movieRepository.findAll();
        return ResponseEntity.ok().body(movies);
    }

    public ResponseEntity<Movie> moviesIdDelete(@Parameter(in = ParameterIn.PATH, description = "the id of the movie to delete", required=true, schema=@Schema()) @PathVariable("id") Integer id
) {
		log.info("DELETE /movies id" + id);
		
		String token = request.getHeader("access_token");
		
		if (token != null && !token.isEmpty()) { // TODO: actual token verification
			// delete the given movie
			movieRepository.deleteById((long) id);
			
			return ResponseEntity.ok().build();
		} else {
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
	}

	public ResponseEntity<Movie> moviesIdGet(
			@Parameter(in = ParameterIn.PATH, description = "the id of the movie to get details about", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
		log.info("GET /movies id " + id);
		
		Optional<Movie> optionalMovie = movieRepository.findById((long) id);
		
		if (optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			return ResponseEntity.ok().body(movie);
		} else {
			return ResponseEntity.ok().build();
		}
	}

    public ResponseEntity<Movie> moviesIdPut(@Parameter(in = ParameterIn.PATH, description = "the id of the movie to update", required=true, schema=@Schema()) @PathVariable("id") Integer id
,@Parameter(in = ParameterIn.QUERY, description = "the title of the movie to update" ,schema=@Schema()) @Valid @RequestParam(value = "title", required = false) String title
,@Parameter(in = ParameterIn.QUERY, description = "the updated director" ,schema=@Schema()) @Valid @RequestParam(value = "director", required = false) String director
,@Parameter(in = ParameterIn.QUERY, description = "the updated genre" ,schema=@Schema()) @Valid @RequestParam(value = "genre", required = false) String genre
,@Parameter(in = ParameterIn.QUERY, description = "the updated rating" ,schema=@Schema()) @Valid @RequestParam(value = "rating", required = false) String rating
,@Parameter(in = ParameterIn.QUERY, description = "the updated length" ,schema=@Schema()) @Valid @RequestParam(value = "length", required = false) String length
,@Parameter(in = ParameterIn.QUERY, description = "the updated review score" ,schema=@Schema()) @Valid @RequestParam(value = "reviewScore", required = false) BigDecimal reviewScore
,@Parameter(in = ParameterIn.QUERY, description = "the updated release date" ,schema=@Schema()) @Valid @RequestParam(value = "releaseDate", required = false) String releaseDate
,@Parameter(in = ParameterIn.QUERY, description = "the updated value for if the movie is currently playing" ,schema=@Schema()) @Valid @RequestParam(value = "currentlyPlaying", required = false) Boolean currentlyPlaying
,@Parameter(in = ParameterIn.QUERY, description = "the updated value for if the move is an upcoming release" ,schema=@Schema()) @Valid @RequestParam(value = "upcomingRelease", required = false) Boolean upcomingRelease
) {
		log.info("PUT /movies id" + id);
		
		String token = request.getHeader("access_token");
		
		if (token != null && !token.isEmpty()) { // TODO: actual token verification
			// get the movie to update
			Optional<Movie> optionalMovie = movieRepository.findById((long) id);
			if (optionalMovie.isPresent()) {
				Movie toUpdate = optionalMovie.get();
				
				// update the provided attributes
				if (title != null) {
					toUpdate.setTitle(title);
				}
				if (director != null) {
					toUpdate.setDirector(director);
				}
				if (genre != null) {
					toUpdate.setGenre(genre);
				}
				if (rating != null) {
					toUpdate.setRating(rating);
				}
				if (length != null) {
					toUpdate.setLength(length);
				}
				if (reviewScore != null) {
					toUpdate.setReviewScore(reviewScore);
				}
				if (releaseDate != null) {
					toUpdate.setReleaseDate(LocalDate.parse(releaseDate));
				}
				if (currentlyPlaying != null) {
					toUpdate.setCurrentlyPlaying(currentlyPlaying);
				}
				if (upcomingRelease != null) {
					toUpdate.setUpcomingRelease(upcomingRelease);
				}
				
				// save the updated movie
				movieRepository.save(toUpdate);
				
				// return the updated movie
				return ResponseEntity.ok().body(toUpdate);
			} else {
				return ResponseEntity.ok().build();
			}
		} else {
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
	}

    public ResponseEntity<Movie> moviesPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody MovieRequestBody body
) {
		log.info("POST /movies " + body.toString());
		
		String token = request.getHeader("access_token");
		
		if (token != null && !token.isEmpty()) { // TODO: actual token verification
			// convert MovieRequestBody to Movie
			Movie movie = new Movie();
			movie.setTitle(body.getTitle());
			movie.setDirector(body.getDirector());
			movie.setGenre(body.getGenre());
			movie.setRating(body.getRating());
			movie.setLength(body.getLength());
			movie.setRating(body.getRating());
			movie.setReleaseDate(body.getReleaseDate());
			movie.setCurrentlyPlaying(body.isCurrentlyPlaying());
			movie.setUpcomingRelease(body.isUpcomingRelease());
			
			// save the movie
			movieRepository.save(movie);
			
			// build URI for newly-created movie
			String host = System.getProperty("host", "localhost");
			String port = System.getProperty("port", "8080");
			String baseUrl = "http://{host}:{port}/" + API_PATH + "movies/";
			
			URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).host(host).port(port).path("{id}").build(movie.getId());
			
			return ResponseEntity.created(uri).body(movie);
		} else {
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
	}

}
package io.swagger.api;

import io.swagger.jpa.MembershipRepository;
import io.swagger.model.JwtResponse;
import io.swagger.model.LoginRequest;
import io.swagger.model.Membership;
import io.swagger.model.MembershipRequestBody;
import io.swagger.security.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
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
        log.info("POST /login " + loginRequest.toString());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtResponse(token));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid email or password!" + e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> createMembership(@Valid @RequestBody MembershipRequestBody membershipRequestBody) {
        log.info("POST /register " + membershipRequestBody.toString());
        log.info("Checking if email is already taken");
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
}package io.swagger.api;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
public class ApiOriginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.54).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Membership;
import io.swagger.model.MembershipRequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-25T21:59:48.447063-04:00[America/New_York]")
@Validated
public interface MembershipApi {

    @Operation(summary = "Get details of a membership", description = "Returns details of a specific membership", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successfully retrieved membership details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Membership.class))) })
    @RequestMapping(value = "/membership/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Membership> membershipIdGet(@Parameter(in = ParameterIn.PATH, description = "the id of the membership", required=true, schema=@Schema()) @PathVariable("id") Integer id
);


    @Operation(summary = "Create a membership", description = "Create a new membership", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "successfully created membership", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Membership.class))) })
    @RequestMapping(value = "/membership",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Membership> membershipPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody MembershipRequestBody body
);

}

/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.54).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@Validated
public interface TheaterBoxesApi {

    @Operation(summary = "Get details about a specific theater box", description = "Returns details about a specific theater box.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Details about the theater box.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TheaterBox.class))) })
    @RequestMapping(value = "/theater_boxes/{box_number}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<TheaterBox> theaterBoxesBoxNumberGet(@Parameter(in = ParameterIn.PATH, description = "Number of the theater box to retrieve details for.", required=true, schema=@Schema()) @PathVariable("box_number") Integer boxNumber
);


    @Operation(summary = "Get details of all theater boxes", description = "Returns details of all theater boxes.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A list of theater boxes.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TheaterBox.class)))) })
    @RequestMapping(value = "/theater_boxes",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<TheaterBox>> theaterBoxesGet();


    @Operation(summary = "Create a new theater box", description = "Allows an admin to create a new theater box.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Theater box successfully created.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TheaterBox.class)))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized. Admin access token is invalid."),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action.") })
    @RequestMapping(value = "/theater_boxes",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<List<TheaterBox>> theaterBoxesPost(@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody TheaterBox body
);

}

package io.swagger.api;

import io.swagger.jpa.TheaterBoxRepository;
import io.swagger.model.TheaterBox;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@RestController
public class TheaterBoxesApiController implements TheaterBoxesApi {

	private static final Logger log = LoggerFactory.getLogger(TheaterBoxesApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	private TheaterBoxRepository theaterBoxRepository;

	@org.springframework.beans.factory.annotation.Autowired
	public TheaterBoxesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<TheaterBox> theaterBoxesBoxNumberGet(
			@Parameter(in = ParameterIn.PATH, description = "Number of the theater box to retrieve details for.", required = true, schema = @Schema()) @PathVariable("box_number") Integer boxNumber) {

			try {
				TheaterBox theaterBox = theaterBoxRepository.findByBoxNumber(boxNumber);
				if (theaterBox != null) {
					return new ResponseEntity<>(theaterBox, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				log.error("Error retrieving theater box details", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}

	public ResponseEntity<List<TheaterBox>> theaterBoxesGet() {

			try {
				List<TheaterBox> theaterBoxes = theaterBoxRepository.findAll();
				return new ResponseEntity<>(theaterBoxes, HttpStatus.OK);
			} catch (Exception e) {
				log.error("Error retrieving theater boxes", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}


	public ResponseEntity<List<TheaterBox>> theaterBoxesPost(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody TheaterBox body
			) {

			try {


				// Save the new theater box to the database
				TheaterBox savedTheaterBox = theaterBoxRepository.save(body);

				// Return the newly created theater box in the response
				List<TheaterBox> theaterBoxList = Collections.singletonList(savedTheaterBox);
				return new ResponseEntity<>(theaterBoxList, HttpStatus.CREATED);
			} catch (Exception e) {
				log.error("Error creating new theater box", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}

}
package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
package io.swagger.api;

import io.swagger.jpa.ReservationRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.jpa.TheaterBoxRepository;
import io.swagger.model.Reservation;
import io.swagger.model.ReservationModifyBody;
import io.swagger.model.ReservationReserveBody;
import io.swagger.model.Showtime;
import io.swagger.model.TheaterBox;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
//import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.threeten.bp.OffsetDateTime;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@RestController
public class ReservationApiController implements ReservationApi {

	private static final Logger log = LoggerFactory.getLogger(ReservationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TheaterBoxRepository theaterBoxRepository;

	@org.springframework.beans.factory.annotation.Autowired
	public ReservationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

		public ResponseEntity<Reservation> reservationCancelDelete(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) Long theaterBoxId
	,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
	,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to cancel." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) Long reservationId
	) {
					// Check if the reservation exists
					Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
					if (!optionalReservation.isPresent()) {
						return ResponseEntity.notFound().build();
					}
					Reservation reservation = optionalReservation.get();

					// Check if the showtime has already occurred
					Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
					if (optionalShowtime.isPresent()) {
						Showtime showtime = optionalShowtime.get();
						if (showtime.getDateTime().isBefore(OffsetDateTime.now())) {
							return ResponseEntity.status(HttpStatus.CONFLICT).build();
						}
					} else {
						return ResponseEntity.notFound().build();
					}

					// Update the reserved seats in the theater box
					TheaterBox retrievedTheaterBox = reservation.getTheaterBox();
					retrievedTheaterBox.setReservedSeats(retrievedTheaterBox.getReservedSeats() - reservation.getSeatsReserved());
					theaterBoxRepository.save(retrievedTheaterBox);

					// Delete the reservation
					reservationRepository.delete(reservation);

					return ResponseEntity.ok(reservation);
				}
		

		public ResponseEntity<Reservation> reservationModifyPut(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Number of the theater box." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) Long theaterBoxId
				,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
				,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to modify." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) Long reservationId
				,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationModifyBody body
				) {
					// Check if the reservation exists
					Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
					if (!optionalReservation.isPresent()) {
						return ResponseEntity.notFound().build();
					}
					Reservation reservation = optionalReservation.get();

					// Check if the showtime has already occurred
					Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
					if (optionalShowtime.isPresent()) {
						Showtime showtime = optionalShowtime.get();
						if (showtime.getDateTime().isBefore(OffsetDateTime.now())) {
							return ResponseEntity.status(HttpStatus.CONFLICT).build();
						}
					} else {
						return ResponseEntity.notFound().build();
					}

					// Check if the requested number of seats is available
					int availableSeats = reservation.getTheaterBox().getTotalSeats() - reservation.getTheaterBox().getReservedSeats() + reservation.getSeatsReserved();
					if (body.getSeats() > availableSeats) {
						return ResponseEntity.status(HttpStatus.CONFLICT).build();
					}

					// Update the reservation
					int seatsChange = body.getSeats() - reservation.getSeatsReserved();
					reservation.setSeatsReserved(body.getSeats());
					reservationRepository.save(reservation);

					// Update the reserved seats in the theater box
					TheaterBox retrievedTheaterBox = reservation.getTheaterBox();
					retrievedTheaterBox.setReservedSeats(retrievedTheaterBox.getReservedSeats() + seatsChange);
					theaterBoxRepository.save(retrievedTheaterBox);

					return ResponseEntity.ok(reservation);
		}
			

	public ResponseEntity<Reservation> reservationReservePost(
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = true, schema = @Schema()) @Valid @RequestParam(value = "theater_box", required = true) Long theaterBoxId,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime.", required = true, schema = @Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody ReservationReserveBody body) {
				// Check if the showtime exists
				Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
				if (!optionalShowtime.isPresent()) {
					return ResponseEntity.notFound().build();
				}
				Showtime showtime = optionalShowtime.get();

				// Check if the theater box exists
				Optional<TheaterBox> optionalTheaterBox = theaterBoxRepository.findById(theaterBoxId);
				if (!optionalTheaterBox.isPresent()) {
					return ResponseEntity.notFound().build();
				}
				TheaterBox retrievedTheaterBox = optionalTheaterBox.get();

				// Check if the requested number of seats is available
				int availableSeats = retrievedTheaterBox.getTotalSeats() - retrievedTheaterBox.getReservedSeats();
				if (body.getSeats() > availableSeats) {
					return ResponseEntity.status(HttpStatus.CONFLICT).build();
				}

				// Create the reservation
				Reservation reservation = new Reservation();
				reservation.setShowtime(showtime);
				reservation.setTheaterBox(retrievedTheaterBox);
				reservation.setSeatsReserved(body.getSeats());
				reservationRepository.save(reservation);

				// Update the reserved seats in the theater box
				retrievedTheaterBox.setReservedSeats(retrievedTheaterBox.getReservedSeats() + body.getSeats());
				theaterBoxRepository.save(retrievedTheaterBox);

				return ResponseEntity.ok(reservation);
			}
	}



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
}