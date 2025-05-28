package io.queberry.que.controller;

import io.queberry.que.config.LdapConfiguration;
import io.queberry.que.repository.LdapConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LdapConfigurationController {
    private final LdapConfigurationRepository ldapConfigurationRepository;

    public LdapConfigurationController(LdapConfigurationRepository ldapConfigurationRepository) {
        this.ldapConfigurationRepository = ldapConfigurationRepository;
    }

    @GetMapping("/config/ldap")
    public ResponseEntity getSmsConfig(){
        return ResponseEntity.ok(ldapConfigurationRepository.findAll().stream().findFirst().orElse(new LdapConfiguration()));
    }


    @PutMapping("/config/ldap")
    public ResponseEntity editSmsConfig(@RequestBody LdapConfiguration resource){
        LdapConfiguration ldapConfiguration = ldapConfigurationRepository.findAll().stream().findFirst().orElse(new LdapConfiguration());
        ldapConfiguration.change(resource);
        ldapConfiguration = ldapConfigurationRepository.save(ldapConfiguration);
        return ResponseEntity.ok(ldapConfiguration);
    }
}
