package io.queberry.que.Entity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LdapConfiguration extends AggregateRoot<LdapConfiguration> {

    private boolean enabled;
    private String host;
    private String base;
    private String param;
    private String value;
    private String username;
    private String password;
    private String nameParam;
    private String loginParam;

    public LdapConfiguration change(LdapConfiguration ldapConfiguration){
        this.enabled = ldapConfiguration.isEnabled();
        this.host = ldapConfiguration.getHost();
        this.base = ldapConfiguration.getBase();
        this.param = ldapConfiguration.getParam();
        this.value = ldapConfiguration.getValue();
        this.username = ldapConfiguration.getUsername();
        this.password = ldapConfiguration.getPassword();
        this.nameParam = ldapConfiguration.getNameParam();
        this.loginParam = ldapConfiguration.getLoginParam();
        return this;
    }
}
