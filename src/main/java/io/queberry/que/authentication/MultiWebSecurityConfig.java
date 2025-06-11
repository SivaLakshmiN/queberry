//package io.queberry.que.authentication;
//
//import io.queberry.que.config.JwtTokenUtil;
//import jakarta.servlet.Filter;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.session.HttpSessionEventPublisher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Slf4j
//@Configuration
//@Profile("!ldap")
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class MultiWebSecurityConfig extends WebSecurityConfiguration {
//    //
//    private final UserDetailsService userDetailsService;
//
//    private static JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//
//    private final JwtRequestFilter jwtRequestFilter;
//
//    private final Filter filter;
//
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//
//    //
////    @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder auth)  {
////        auth.authenticationProvider(authProvider());
////    }
////
////
////    @Bean
////    public static DefaultRolesPrefixPostProcessor defaultRolesPrefixPostProcessor() {
////        return new DefaultRolesPrefixPostProcessor();
////    }
//    //
//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    //
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
//
//    @Bean
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
////
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////        http = http.cors().and().csrf().disable();
////
////        // Set session management to stateless
////        http = http
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and();
////
////        // Set unauthorized requests exception handler
////        http = http
////                .exceptionHandling()
////                .authenticationEntryPoint(
////                        (request, response, ex) -> {
////                            response.sendError(
////                                    HttpServletResponse.SC_UNAUTHORIZED,
////                                    ex.getMessage()
////                            );
////                        }
////                )
////                .and();
////
////        // Set permissions on endpoints
////        http.authorizeRequests()
////                // Our public endpoints
////
////                .antMatchers("/api/licenses","/api/tenants","/api/login","/api/greeter/login","/api/tokenanalysisService",
////                        "/api/verify","/api/logout","/api/customerSignup" ,"/api/customerLogin", "/api/customerExists","/push/**",
////                        "/api/dispenser/**","/api/signage/**", "/api/devices/**", "/api/device/**" ,"/api/device", "/api/countries",
////                        "/api/locales", "/api/timezones","/api/download/**", "/announcements/**","/api/surveys/*", "/api/reporting/report/live",
////                        "/api/slots/**","/api/appointments/**","/api/branches/active", "/api/branch/services/active","/api/services/appointmentServices","/api/azure/login","/api/passwordPolicy",
////                        "/api/surveys", "/api/surveys/**", "/api/medias", "/api/config/dispenser/migrate","/api/branch/services/**","/api/branch/branchCapacity/**").permitAll()
////                // Our private endpoints
////                .anyRequest().authenticated();
////
////        // Add JWT token filter
////        http.addFilterBefore(
////                jwtRequestFilter,
////                UsernamePasswordAuthenticationFilter.class
////        );
////    }
//
////    @Bean
////    public CorsFilter corsFilter() {
////        UrlBasedCorsConfigurationSource source =
////                new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.addAllowedOrigin("*");
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("*");
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter(source);
////    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//                }))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/licenses", "/api/tenants", "/api/login", "/api/greeter/login", "/api/tokenanalysisService",
//                                "/api/verify", "/api/logout", "/api/customerSignup", "/api/customerLogin", "/api/customerExists", "/push/**",
//                                "/api/dispenser/**", "/api/signage/**", "/api/devices/**", "/api/device/**", "/api/device", "/api/countries",
//                                "/api/locales", "/api/timezones", "/api/download/**", "/announcements/**", "/api/surveys/*", "/api/reporting/report/live",
//                                "/api/slots/**", "/api/appointments/**", "/api/branches/active", "/api/branch/services/active", "/api/services/appointmentServices", "/api/azure/login", "/api/passwordPolicy",
//                                "/api/surveys", "/api/surveys/**", "/api/medias", "/api/config/dispenser/migrate", "/api/branch/services/**", "/api/branch/branchCapacity/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:30001"); // Your frontend URL
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//
////    @Bean
////    public CorsFilter corsFilter() {
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.addAllowedOrigin("http://localhost:30001"); // Your frontend URL
////        config.addAllowedMethod("*");
////        config.addAllowedHeader("*");
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter(source);
////    }
//
//
////    @Configuration
////    @Order(1)
////    @Profile("!ldap")
////    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
////
////        @Override
////        protected void configure(HttpSecurity http) throws Exception {
//////            http
////                    //Configure HTTP Basic Authentication
//////                    .httpBasic()
////
////                    //Exemptions
//////                    .and().antMatcher("/api/**").authorizeRequests().antMatchers("/api/time").permitAll()
//////
//////
////            http.csrf().disable()
////                    // dont authenticate this particular request
////                    .antMatcher("/api/**")
////                    .authorizeRequests()
////
////
////                    .and().authorizeRequests().antMatchers("/js/**", "/navigation/**", "/css/**", "/images/**", "/template/**", "/webjars/**","/assets/**","/assets/**/**", "/favicon.ico","/**/favicon.*","/error", "/actuator/**").permitAll()
//////                    .and().authorizeRequests().antMatchers("/api/login","/api/devices/**","/api/dispenser/**","/api/signage/**","/push/**").permitAll()
////
////                    .and().authorizeRequests().antMatchers("/api/counters/**","/api/refreshtoken","/api/login","/api/logout","/api/assistance/updateDtls","/api/dispenser/**","/api/signage/**", "/api/devices/**","/push/**","/api/time", "/api/download/**","/api/config","/api/config/**", "/announcements/**", "/api/devices/register" ,"/api/device","/api/device/reject","/api/reporting/report/live", "/api/device/**", "/api/surveys/**","/api/login/logo","/api/ivr/callback","/api/crm/**").permitAll()
//////
//////                    .and().authorizeRequests().antMatchers("/api/**").permitAll()
//////
////////                    .and().authorizeRequests().antMatchers("/api/assistance/updateDtls","/api/dispenser/**","/api/signage/**", "/api/devices/**","/push/**","/api/time", "/api/download/**","/api/config","/api/config/**", "/announcements/**", "/api/devices/register" ,"/api/device","/api/device/reject","/api/reporting/report/live", "/api/device/**", "/api/surveys/**","/api/login/logo","/api/ivr/callback","/api/crm/**").permitAll()
//////
//////
////                   .and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//////
//////                    //for feedback
//////                    .and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/surveys/{\\d+}","/takeSurvey**","/loading**").permitAll()
//////                    .and().authorizeRequests().antMatchers(HttpMethod.PUT, "/api/surveys/{\\d+}/responses").permitAll()
//////
////
//////                    .antMatchers("/login","/logout").permitAll()
////                    .anyRequest().authenticated()
////                    .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
////
////
////
////
////                    //Configure the path for Basic Authentication
//////                    .and().authorizeRequests().anyRequest().authenticated()
////
////                    //Disable CSRF and FrameOptions for H2
//////                    .and().csrf().disable().headers().frameOptions().disable()
//////                            .and().cors().disable();
////
////
////                    //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////
////                    //Disable Basic Authentication for OPTIONS
////
////                    //Added headers for CORS
////                    // disable when angular runs in localhost
//////                    .and().headers().addHeaderWriter((request, response) -> {
////////                response.addHeader("Access-Control-Allow-Origin", "*");
//////                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
//////                response.setHeader("Access-Control-Max-Age", "3600");
//////                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//////            });
////
////
////
////            log.info("Basic Authentication Configured for /api/**");        }
//////    }
//
////    @Configuration
////    @Profile("!ldap")
////    @RequiredArgsConstructor
////    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
////
////        private final SessionRegistry sessionRegistry;
////        private final LogoutEventHandler logoutEventHandler;
////
////        @Override
////        protected void configure(HttpSecurity http) throws Exception {
////            http
////
//////                    Configuring Form Login
////                    .formLogin().loginPage("/login").permitAll()
////
//////                    Configuring Logout
////                    .and().logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutEventHandler).permitAll()
////
//////                    ui exemption
////                    .and().authorizeRequests().antMatchers("/js/**", "/navigation/**", "/css/**", "/images/**", "/template/**", "/webjars/**","/assets/**","/assets/**/**", "/favicon.ico","/**/favicon.*","/error").permitAll()
////
//////                    for dispenser apis , login , device endpoints
//////                     /announcements/** for signage announcements
////                    .and().authorizeRequests().antMatchers("/api/assistance/updateDtls","/api/dispenser/**","/api/signage/**", "/api/devices/**","/push/**","/api/time","/api/signage/playlist", "/api/download/**","/api/config","/api/config/**", "/announcements/**", "/api/devices/register","/api/device","/api/device/reject","/api/reporting/report/live", "/api/device/**", "/api/surveys/**","/api/login/logo","/api/ivr/callback").permitAll()
////
//////                    for feedback
////                    .and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/surveys/{\\d+}","/survey/**","/takeSurvey**","/loading**","/api/surveys/**").permitAll()
////                    .and().authorizeRequests().antMatchers(HttpMethod.PUT, "/api/surveys/{\\d+}/responses").permitAll()
////
//////                    Enable authentication for the rest of the end points
////                    .and().authorizeRequests().anyRequest().authenticated()
////
////                    //disable csrf and frame options for H2
////                    .and().csrf().disable().headers().frameOptions().disable()
////
////                    .and().sessionManagement()
////
////                    .maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry)
////
////                    ;
////
////            log.info("Form Authentication Configured for /**");
////        }
////    }
//}

package io.queberry.que.authentication;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Slf4j
@Configuration
@Profile("!ldap")
@CrossOrigin
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class MultiWebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://192.168.1.7:30001"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .cors(cors -> cors.disable())
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/licenses", "/api/tenants", "/api/login", "/api/greeter/login", "/api/tokenanalysisService",
                                "/api/verify","/api/roles", "/api/logout", "/api/customerSignup", "/api/customerLogin", "/api/customerExists", "/push/**",
                                "/api/dispenser/**", "/api/signage/**", "/api/devices/**", "/api/device/**", "/api/device", "/api/countries",
                                "/api/locales", "/api/timezones", "/api/download/**", "/announcements/**", "/api/surveys/*", "/api/reporting/report/live",
                                "/api/slots/**", "/api/appointments/**", "/api/branches/active", "/api/branch/services/active", "/api/services/appointmentServices", "/api/azure/login", "/api/passwordPolicy",
                                "/api/surveys", "/api/surveys/**", "/api/medias", "/api/config/dispenser/migrate", "/api/branch/services/**", "/api/branch/branchCapacity/**","/api/refreshtoken"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}



