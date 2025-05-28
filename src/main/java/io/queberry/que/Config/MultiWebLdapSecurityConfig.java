//package io.queberry.que.Config;
//
//import io.queberry.que.auth.LogoutEventHandler;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
//import org.springframework.security.web.session.HttpSessionEventPublisher;
//
//import javax.naming.Context;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Configuration
//@Profile("ldap")
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class MultiWebLdapSecurityConfig {
//
//    @Value("#{environment['LDAP_DOMAIN']}")
//    private String domain;
//
//    @Value("#{environment['LDAP_URL']}")
//    private String url;
//
//    @Value("#{environment['LDAP_USERNAME']}")
//    private String ldapUsername;
//
//    @Value("#{environment['LDAP_PASSWORD']}")
//    private String ldapPassword;
//
//    @Value("#{environment['LDAP_SEARCH']}")
//    private String ldapsearch;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws IOException {
//        auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
//    }
//
//    @Bean
//    public static DefaultRolesPrefixPostProcessor defaultRolesPrefixPostProcessor() {
//        return new DefaultRolesPrefixPostProcessor();
//    }
//
//    @Bean
//    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider()
//            throws IOException {
//        log.info("Domain : {}, Url : {}, Manager Username : {}, Manager Password", domain, url, ldapUsername, ldapPassword);
//        if (url == null)
//            throw new RuntimeException("Ldap Configuration Error. Please check and restart");
//        ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider =
//                new ActiveDirectoryLdapAuthenticationProvider(domain, url);
//        activeDirectoryLdapAuthenticationProvider.setConvertSubErrorCodesToExceptions(true);
//        activeDirectoryLdapAuthenticationProvider.setUseAuthenticationRequestCredentials(true);
//        if (ldapsearch != null) {
//            activeDirectoryLdapAuthenticationProvider.setSearchFilter(ldapsearch);
//        }
//        Map<String, Object> contextEnvironmentProperties = new HashMap<>();
//        if (ldapUsername != null) {
//            contextEnvironmentProperties.put(Context.SECURITY_PRINCIPAL, ldapUsername);
//        }
//        if (ldapPassword != null) {
//            contextEnvironmentProperties.put(Context.SECURITY_CREDENTIALS, ldapPassword);
//        }
//        if (!contextEnvironmentProperties.isEmpty()) {
//            activeDirectoryLdapAuthenticationProvider.setContextEnvironmentProperties(contextEnvironmentProperties);
//        }
//        return activeDirectoryLdapAuthenticationProvider;
//    }
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher()
//    {
//        return new HttpSessionEventPublisher();
//    }
//
//    @Bean
//    public SessionRegistry sessionRegistry(){
//        return new SessionRegistryImpl();
//    }
//
//    @Configuration
//    @Order(1)
//    @Profile("ldap")
//    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    //Configure HTTP Basic Authentication
//                    .httpBasic()
//
//                    //Exemptions
//                    .and().antMatcher("/api/**").authorizeRequests().antMatchers("/api/time").permitAll()
//
//
//                    .and().authorizeRequests().antMatchers("/api/dispenser/**","/api/signage/**", "/api/devices/**","/push/**","/api/time", "/api/download/**","/api/config","/api/config/**", "/announcements/**", "/api/devices/register" ,"/api/device","/api/device/reject","/api/reporting/report/live", "/api/device/**", "/api/surveys/**").permitAll()
//
//                    .and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                    //for feedback
//                    .and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/surveys/{\\d+}","/takeSurvey**","/loading**").permitAll()
//                    .and().authorizeRequests().antMatchers(HttpMethod.PUT, "/api/surveys/{\\d+}/responses").permitAll()
//
//
//                    //Configure the path for Basic Authentication
//                    .and().authorizeRequests().anyRequest().authenticated()
//
//                    //Disable CSRF and FrameOptions for H2
//                    .and().csrf().disable().headers().frameOptions().disable()
//
//                    //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                    //Disable Basic Authentication for OPTIONS
//
//                    //Added headers for CORS
//                    .and().headers().addHeaderWriter((request, response) -> {
//                response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
//                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
//                response.setHeader("Access-Control-Max-Age", "3600");
//                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//            });
//
//            log.info("Basic Authentication Configured for /api/**");        }
//    }
//
//    @Configuration
//    @Profile("ldap")
//    @RequiredArgsConstructor
//    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        private final SessionRegistry sessionRegistry;
//        private final LogoutEventHandler logoutEventHandler;
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//
//                    //Configuring Form Login
//                    .formLogin().loginPage("/login").defaultSuccessUrl("/index.html",true).usernameParameter("username").passwordParameter("password").permitAll()
//
////                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                    //Configuring Logout
//                    .and().logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutEventHandler).permitAll()
//
//                    //ui exemption
//                    .and().authorizeRequests().antMatchers("/js/**", "/navigation/**", "/css/**", "/images/**", "/template/**", "/webjars/**","/assets/**","/assets/**/**", "/favicon.ico","/**/favicon.*","/error").permitAll()
//
//                    //for dispenser apis , login , device endpoints
//                    // /announcements/** for signage announcements
//                    .and().authorizeRequests().antMatchers("/api/dispenser/**","/api/signage/**", "/api/devices/**","/push/**","/api/time","/api/signage/playlist", "/api/download/**","/api/config","/api/config/**", "/announcements/**", "/api/devices/register","/api/device","/api/device/reject","/api/reporting/report/live", "/api/device/**", "/api/surveys/**", "/api/crm/**").permitAll()
//
//                    //for feedback
//                    .and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/surveys/{\\d+}","/survey/**","/takeSurvey**","/loading**").permitAll()
//                    .and().authorizeRequests().antMatchers(HttpMethod.PUT, "/api/surveys/{\\d+}/responses").permitAll()
//                    //Enable authentication for the rest of the end points
//                    .and().authorizeRequests().anyRequest().authenticated()
//
//                    //disable csrf and frame options for H2
//                    .and().csrf().disable().headers().frameOptions().disable();
//
//                    //.and().sessionManagement()
//
//                    //.maximumSessions(1).maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry)
//
//
//
//            log.info("Form Authentication Configured for /**");
//        }
//    }
//}
