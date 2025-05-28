//package io.queberry.que.Config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//
//@Slf4j
//@Configuration
//@EnableWebSocketMessageBroker
////public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/push")
//                .setAllowedOrigins("*")
//                .withSockJS();
////        registry.addEndpoint("/push");
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/device");
//        registry.enableSimpleBroker("/notifications");
//    }

//    @EventListener
//    public void handleSubscribeEvent(SessionSubscribeEvent event) {
//        log.info("<==> handleSubscribeEvent: username=" + event.getUser().getName() + ", event=" +
//                event);
//    }
//
//    @EventListener
//    public void handleConnectEvent(SessionConnectEvent event) {
//        log.info("===> handleConnectEvent: username=" + event.getUser().getName() + ", event=" +
//                event);
//    }
//
//    @EventListener
//    public void handleDisconnectEvent(SessionDisconnectEvent event) {
//        log.info("<=== handleDisconnectEvent: username=" + event.getUser().getName() + ", event="
//                + event);
//    }
//}