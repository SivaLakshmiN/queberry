package io.queberry.que.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.naming.Context;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("ldap")
@EnableWebSecurity
@RequiredArgsConstructor
public class MultiWebLdapSecurityConfig {

    @Value("${LDAP_DOMAIN}")
    private String domain;

    @Value("${LDAP_URL}")
    private String url;

    @Value("${LDAP_USERNAME}")
    private String ldapUsername;

    @Value("${LDAP_PASSWORD}")
    private String ldapPassword;

    @Value("${LDAP_SEARCH}")
    private String ldapSearch;

    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        if (url == null) {
            throw new RuntimeException("Ldap Configuration Error. Please check and restart");
        }
        ActiveDirectoryLdapAuthenticationProvider provider =
                new ActiveDirectoryLdapAuthenticationProvider(domain, url);
        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        if (ldapSearch != null) {
            provider.setSearchFilter(ldapSearch);
        }
        Map<String, Object> contextProperties = new HashMap<>();
        if (ldapUsername != null) {
            contextProperties.put(Context.SECURITY_PRINCIPAL, ldapUsername);
        }
        if (ldapPassword != null) {
            contextProperties.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        }
        if (!contextProperties.isEmpty()) {
            provider.setContextEnvironmentProperties(contextProperties);
        }
        return provider;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .cors(Customizer.withDefaults()) // enable CORS
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())    // disable CSRF using lambda
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // allow CORS preflight
                        .requestMatchers("/api/login","/api/main/config","/api/verify","/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
    }
}