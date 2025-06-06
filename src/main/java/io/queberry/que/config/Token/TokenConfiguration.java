package io.queberry.que.config.Token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.config.ConfigurationEvent;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Getter
@ToString
//@NoArgsConstructor(force = true,access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TokenConfiguration extends AggregateRoot<TokenConfiguration> {
    private Integer tokenValidity;


    public TokenConfiguration change(TokenConfigurationController.TokenConfigurationResource resource){
        this.tokenValidity = resource.getTokenValidity();
        return andEvent(ConfigurationEvent.of(this));
    }

    public TokenConfiguration(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
        registerEvent(ConfigurationEvent.of(this));

    }

    @Override
    @JsonIgnore
    public String getId(){
        return super.getId();
    }

    @Override
    @JsonIgnore
    public Long getVersion(){
        return super.getVersion();
    }
}
