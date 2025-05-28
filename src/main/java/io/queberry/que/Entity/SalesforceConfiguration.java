package io.queberry.que.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.Controller.SalesforceConfigurationController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class SalesforceConfiguration extends AggregateRoot<SalesforceConfiguration> {

    @Column(columnDefinition = "bit default 0")
    private boolean enabled;
    private String url;
    private String clientId;
    private String clientSecret;

    public SalesforceConfiguration change(SalesforceConfigurationController.SalesforceConfigurationResource resource){
        this.enabled = resource.isEnabled();
        this.url = resource.getUrl();
        this.clientId = resource.getClientId();
        this.clientSecret = resource.getClientSecret();
        return this;
    }

    public SalesforceConfiguration(boolean enabled, String url, String clientId, String clientSecret) {
        this.enabled = enabled;
        this.url = url;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
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
