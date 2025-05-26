package io.queberry.que.Entity;//
//import com.example.QueApplication.Entity.AggregateRoot;
//import com.example.QueApplication.Entity.Service;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//
//import java.time.LocalDateTime;
//import java.util.Objects;
//
///**
// * @author : Fahad Fazil
// * @since : 30/01/18
// */
//@Entity
//@Getter
//@ToString
//@Slf4j
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true,of = "createdAt")
//@Table(indexes = {@Index(name="idx_token_createdAt", columnList = "createdAt DESC"),
//        @Index(name="idx_token_branch_service", columnList = "createdAt DESC, branch, service_id"),
//        @Index(name="idx_token_createdAt_service_number_branch_status", columnList = "createdAt DESC, service_id, number, branch, status")})
//public class Token extends AggregateRoot<Token> implements Comparable<Token>{
//
//    @ManyToOne
//    private Service service;
//
//    @ManyToOne
//    private SubService subService;
//
//    private Integer number;
//
//    @Enumerated(EnumType.STRING)
//    private TokenStatus status;
//
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Enumerated(EnumType.STRING)
//    private Type type;
//
//    @Enumerated(EnumType.STRING)
//    private Medium medium;
//
//    private String language;
//
//    private String mobile;
//
//    @OneToOne
//    private Appointment appointment;
//
//    private String branch;
//
//    private Integer priority;
//
//    private String privateCode;
//
//    public String getToken(){
//        return getString(service, number, privateCode);
////        return service.getCode() +service.getSeperator()+ number.toString();
//    }
//
//    @NotNull
//    public static String getString(Service service, Integer number, String privateCode) {
//        String formattedNumber;
//        String code = null;
//        String separator = null;
//        if (service.isSharedSeq()) {
//            if (service.getSharedSequence().isFormat()) {
//                formattedNumber = String.format("%0" + service.getSharedSequence().getSequenceEnd().toString().length() + "d", number);
//            } else {
//                formattedNumber = number.toString();
//            }
//            if (service.getSharedSequence().isServiceCode()) {
//                code = service.getCode();
//                separator = service.getSeperator();
////                return service.getCode() + service.getSeperator() + formattedNumber;
//            } else {
//                code = service.getSharedSequence().getCode();
//                separator = service.getSharedSequence().getSeperator();
////                return service.getSharedSequence().getCode() + service.getSharedSequence().getSeperator() + formattedNumber;
//            }
//        } else {
//            if (service.isFormat()) {
//                formattedNumber = String.format("%0" + service.getSequenceEnd().toString().length() + "d", number);
//            } else {
//                formattedNumber = number.toString();
//            }
//            code = service.getCode();
//            separator = service.getSeperator();
////            return service.getCode() + service.getSeperator() + formattedNumber;
//        }
//
////        if (!Objects.equals(privateCode, "")) {
////            code = privateCode;
////        }
////        if (!Objects.equals(privateCode, null)) {
////            code = privateCode;
////        }
//        if (privateCode != null){
//            code = privateCode;
//        }
//        return code + separator + formattedNumber;
//    }
//
//    public Token(Service service,SubService subService, Integer number, Type type, String language, String mobile, Medium medium, String branch){
//        log.info("token constructoer");
//        this.service = service;
//        this.subService = subService;
//        this.number = number;
//        this.status = TokenStatus.CREATED;
//        this.createdAt = LocalDateTime.now();
//        this.type = type;
//        this.language = language;
//        this.mobile = mobile;
//        this.medium = medium;
//        this.branch = branch;
//        this.priority = 1;
//        this.registerEvent(TokenCreated.of(this));
//    }
//
//    public Token(Service service,SubService subService, Integer number, Type type, String language, String mobile, Medium medium, String branch, Integer priority, String privateCode){
//        log.info("token constructoer");
//        this.service = service;
//        this.subService = subService;
//        this.number = number;
//        this.status = TokenStatus.CREATED;
//        this.createdAt = LocalDateTime.now();
//        this.type = type;
//        this.language = language;
//        this.mobile = mobile;
//        this.medium = medium;
//        this.branch = branch;
//        this.priority = priority;
//        this.privateCode = privateCode;
//        this.registerEvent(TokenCreated.of(this));
//    }
//
//    public Token process(){
//        this.status = TokenStatus.PROCESSING;
//        return this;
//    }
//
//    public Token noShow(){
//        this.status = TokenStatus.NO_SHOW;
//        return this.andEvent(TokenNoShow.of(this));
//    }
//
//    public Token complete(){
//        this.status = TokenStatus.COMPLETED;
//        return this.andEvent(TokenCompleted.of(this));
//    }
//
//    public Token expire(){
//        this.status = TokenStatus.EXPIRED;
//        return this.andEvent(TokenExpired.of(this));
//    }
//
//    public Token appointment(Appointment appointment){
//        this.appointment = appointment;
//        return this;
//    }
//
////    public Service getServiceInfo(){
////        return this.getService();
////    }
//
//    public SubService getSubServiceInfo(){
//        return this.getSubService();
//    }
//
//    public static enum Medium{
//        PAPER,
//        SMS,
//        WHATSAPP
//    }
//
//    @Override
//    public int compareTo(Token o) {
//        return this.createdAt.compareTo(o.createdAt);
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class TokenCreated extends DomainEvent<Token> {
//
//        @AggregateReference
//        final Token token;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class TokenNoShow extends DomainEvent<Token> {
//
//        @AggregateReference
//        final Token token;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class TokenCompleted extends DomainEvent<Token> {
//
//        @AggregateReference
//        final Token token;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class TokenExpired extends DomainEvent<Token> {
//
//        @AggregateReference
//        final Token token;
//    }
//}
