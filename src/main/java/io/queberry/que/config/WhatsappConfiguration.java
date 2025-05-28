package io.queberry.que.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.controller.WhatsappConfigurationController;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class WhatsappConfiguration extends AggregateRoot<WhatsappConfiguration> {
    @Column(columnDefinition = "bit default 0")
    private boolean enabled;
    private String token;
    private String mobile;
    private String businessId;
    private String appId;
    private String tenant;
    private String phoneId;

    public WhatsappConfiguration change(WhatsappConfigurationController.Resource resource){
        this.enabled = resource.isEnabled();
        this.token = resource.getToken();
        this.mobile = resource.getMobile();
        this.businessId = resource.getBusinessId();
        this.appId = resource.getAppId();
        this.phoneId = resource.getPhoneNumberId();
        this.tenant = TenantContext.getCurrentTenant();
        return this;
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
