package io.queberry.que.config.Kpi;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KpiConfiguration extends AggregateRoot<KpiConfiguration> {
    private boolean enabled;
    private String exchangeType;
    private String tenantId;
    private String clientId;
    private String clientSecret;
    private String sender;
    private boolean saveEMail;
    private String protocol;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String fromAddress;
    private boolean ehlo;
    private boolean waitingTime;
    private boolean transactionTime;
    private String emailSubjectBody;
    private String emailUploadId;

    public KpiConfiguration change(KpiConfiguration kpiConfiguration){
        this.enabled = kpiConfiguration.isEnabled();
        this.exchangeType = kpiConfiguration.getExchangeType();
        this.tenantId = kpiConfiguration.getTenantId();
        this.clientId = kpiConfiguration.getClientId();
        this.clientSecret = kpiConfiguration.getClientSecret();
        this.sender = kpiConfiguration.getSender();
        this.saveEMail = kpiConfiguration.isSaveEMail();
        this.protocol = kpiConfiguration.getProtocol();
        this.host = kpiConfiguration.getHost();
        this.port = kpiConfiguration.getPort();
        this.username = kpiConfiguration.getUsername();
        this.password = kpiConfiguration.getPassword();
        this.fromAddress = kpiConfiguration.getFromAddress();
        this.ehlo = kpiConfiguration.isEhlo();
        this.waitingTime = kpiConfiguration.isWaitingTime();
        this.transactionTime = kpiConfiguration.isTransactionTime();
        this.emailSubjectBody=kpiConfiguration.getEmailSubjectBody();
        this.emailUploadId=kpiConfiguration.getEmailUploadId();
        return this;
    }

}
