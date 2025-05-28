package io.queberry.que.Entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.events.EventTarget;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

// currently, considering home as organization
@Entity
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor(force = true)
public class Home extends AggregateRoot<Home>{

    @Column(unique = true)
    private String name;

    private Locale locale;

    private ZoneId timeZoneId;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Address address;

    private LocalTime businessStart;

    private LocalTime businessEnd;

    private String enterpriseBranchId;

    private String enterpriseURL;

    private String enterpriseApiKey;

    private String enterpriseApiSecret;

    private String rabbitMqUsername;

    private String rabbitMqpassword;
    private String domain;
    private String category;
    private String email;
    private String orgLogo;


    public Home(String name, Locale locale, ZoneId timeZoneId, LocalTime businessStart, LocalTime businessEnd){
        log.info("home main"+name);
        this.name = name;
        this.locale = locale;
        this.timeZoneId = timeZoneId;
        this.businessStart = businessStart;
        this.businessEnd = businessEnd;
        this.registerEvent(HomeAdded.of(this));
    }


    public static Home of(String name, Locale locale, ZoneId timeZoneId, LocalTime businessStart, LocalTime businessEnd){
        Home home = new Home(name, locale, timeZoneId,businessStart,businessEnd);
        log.info("home"+home);
        return home;
    }

    public Home(String name, Locale locale, LocalTime businessStart, LocalTime businessEnd){
        this(name, locale, ZoneId.systemDefault(),businessStart,businessEnd);
    }

    public Home(String enterpriseBranchId, String enterpriseURL, String enterpriseApiKey, String enterpriseApiSecret) {
//        log.info("enterprise info"+enterpriseURL+" "+enterpriseBranchId);
        this.enterpriseBranchId = enterpriseBranchId;
        this.enterpriseURL = enterpriseURL;
        this.enterpriseApiKey = enterpriseApiKey;
        this.enterpriseApiSecret = enterpriseApiSecret;
        // this.name = name;
    }

    public Home rabbitMq(String rabbitMqUsername, String rabbitMqpassword){
        log.info("rabbitmq"+rabbitMqUsername);
        this.rabbitMqUsername = rabbitMqUsername;
        this.rabbitMqpassword = rabbitMqpassword;
        return this;
    }

    public static Home of(String name, Locale locale, LocalTime businessStart, LocalTime businessEnd){
        Home home = new Home(name, locale, businessStart, businessEnd);
        return home;
    }


    @JsonCreator
    public static Home of(String name,LocalTime businessStart,LocalTime businessEnd , String enterpriseURL,String enterpriseApiKey,String enterpriseApiSecret,String enterpriseBranchId){
//        log.info("info"+name+" "+businessStart+ " "+businessEnd );
        if (name != null && businessStart != null && businessEnd != null){
            log.info("Home of : 94");
            Home home = new Home(name, Locale.getDefault(),businessStart ,businessEnd);
            return home.andEvent(HomeAdded.of(home));
        }
//        log.info("enterprise"+enterpriseBranchId+" "+enterpriseURL);
        Home home = new Home(enterpriseBranchId,enterpriseURL,enterpriseApiKey,enterpriseApiSecret);
        return home.andEvent(HomeAdded.of(home));

    }

    Home locale(Locale newLocale){
        log.info("locale");
        final Locale oldLocale = (Locale) this.locale.clone();
        this.locale = newLocale;

        this.registerEvent(LocaleChanged.of(getId(), oldLocale, newLocale));
        return this;
    }

    public Home address(Address newAddress)  {
        final Address oldAddress = this.address == null ? null : this.address.clone();
        this.address = newAddress;

        this.registerEvent(AddressChanged.of(getId(), oldAddress, newAddress));
        return this;

    }

    public Home attachToEnterprise(String name, Locale locale, ZoneId timeZoneId, LocalTime businessStart, LocalTime businessEnd,Address address){
        this.name = name;
        locale(locale);
        address(address);
        this.timeZoneId = timeZoneId;
        this.businessStart = businessStart;
        this.businessEnd = businessEnd;
        this.registerEvent(HomeAdded.of(this));
        return andEvent(AttachedToEnterprise.of(this));
    }

    public Home home(Home updated){
        log.info("home updated" + updated );
        this.enterpriseBranchId = updated.getEnterpriseBranchId();
        this.enterpriseURL = updated.getEnterpriseURL();
        this.enterpriseApiKey = updated.getEnterpriseApiKey();
        this.enterpriseApiSecret = updated.getEnterpriseApiSecret();
        this.rabbitMqUsername = updated.getRabbitMqUsername();
        this.rabbitMqpassword = updated.getRabbitMqpassword();
        return this;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class HomeAdded extends DomainEvent<Home>{

        @AggregateReference
        Home home;

        @Override
        public String getType() {
            return "";
        }

        @Override
        public EventTarget getTarget() {
            return null;
        }

        @Override
        public EventTarget getCurrentTarget() {
            return null;
        }

        @Override
        public short getEventPhase() {
            return 0;
        }

        @Override
        public boolean getBubbles() {
            return false;
        }

        @Override
        public boolean getCancelable() {
            return false;
        }

        @Override
        public long getTimeStamp() {
            return 0;
        }

        @Override
        public void stopPropagation() {

        }

        @Override
        public void preventDefault() {

        }

        @Override
        public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {

        }
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    @EqualsAndHashCode(callSuper = true)
    public static class LocaleChanged extends DomainEvent<Home>{
        @AggregateReference
        String homeId;

        Locale oldLocale, newLocale;

        @Override
        public String getType() {
            return "";
        }

        @Override
        public EventTarget getTarget() {
            return null;
        }

        @Override
        public EventTarget getCurrentTarget() {
            return null;
        }

        @Override
        public short getEventPhase() {
            return 0;
        }

        @Override
        public boolean getBubbles() {
            return false;
        }

        @Override
        public boolean getCancelable() {
            return false;
        }

        @Override
        public long getTimeStamp() {
            return 0;
        }

        @Override
        public void stopPropagation() {

        }

        @Override
        public void preventDefault() {

        }

        @Override
        public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {

        }
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    @EqualsAndHashCode(callSuper = true)
    public static class AddressChanged extends DomainEvent<Home>{
        @AggregateReference
        String homeId;

        Address oldAddress, newAddress;

        @Override
        public String getType() {
            return "";
        }

        @Override
        public EventTarget getTarget() {
            return null;
        }

        @Override
        public EventTarget getCurrentTarget() {
            return null;
        }

        @Override
        public short getEventPhase() {
            return 0;
        }

        @Override
        public boolean getBubbles() {
            return false;
        }

        @Override
        public boolean getCancelable() {
            return false;
        }

        @Override
        public long getTimeStamp() {
            return 0;
        }

        @Override
        public void stopPropagation() {

        }

        @Override
        public void preventDefault() {

        }

        @Override
        public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {

        }
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    @EqualsAndHashCode(callSuper = true)
    public static class AttachedToEnterprise extends DomainEvent<Home>{
        @AggregateReference
        Home home;

        @Override
        public String getType() {
            return "";
        }

        @Override
        public EventTarget getTarget() {
            return null;
        }

        @Override
        public EventTarget getCurrentTarget() {
            return null;
        }

        @Override
        public short getEventPhase() {
            return 0;
        }

        @Override
        public boolean getBubbles() {
            return false;
        }

        @Override
        public boolean getCancelable() {
            return false;
        }

        @Override
        public long getTimeStamp() {
            return 0;
        }

        @Override
        public void stopPropagation() {

        }

        @Override
        public void preventDefault() {

        }

        @Override
        public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {

        }
    }
}
