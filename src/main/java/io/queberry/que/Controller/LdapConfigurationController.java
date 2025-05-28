package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.LdapConfiguration;
import io.queberry.que.Repository.LdapConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CloudloomController
@RequiredArgsConstructor
public class LdapConfigurationController {

    private final LdapConfigurationRepository ldapConfigurationRepository;

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
