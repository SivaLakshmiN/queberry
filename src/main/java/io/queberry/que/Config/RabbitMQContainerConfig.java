package io.queberry.que.Config;//package io.queberry.que.config;
//
//import io.queberry.que.enterprise.AggregateRootListener;
//import io.queberry.que.enterprise.AggregateRootService;
//import io.queberry.que.home.Home;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//

//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//
//@Configuration
//@Profile("enterprise")
//@RequiredArgsConstructor
//public class RabbitMQContainerConfig {
//
//    private final Home home;
//    private final AggregateRootService aggregateRootService;
//    private final ConnectionFactory connectionFactory;
//    private final MessageConverter jsonMessageConverter;
//
//    @Bean
//    public SimpleMessageListenerContainer container() throws NoSuchAlgorithmException, KeyManagementException {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        MessageListenerAdapter messageListenerAdapter = new  MessageListenerAdapter(new AggregateRootListener(aggregateRootService), "handleMessage");
//        messageListenerAdapter.setMessageConverter(jsonMessageConverter);
//        container.setMessageListener(messageListenerAdapter);
//        String[] queueNames = {"Service:"+home.getEnterpriseBranchId(), "Counter:"+home.getEnterpriseBranchId(),"Device:"+home.getEnterpriseBranchId(), "Events:"+home.getEnterpriseBranchId(),"Media:"+home.getEnterpriseBranchId(),
//                "Playlist:"+home.getEnterpriseBranchId(),"Role:"+home.getEnterpriseBranchId(),"ServiceGroup:"+home.getEnterpriseBranchId(),
//                "Template:"+home.getEnterpriseBranchId(),"Ticker:"+home.getEnterpriseBranchId(),"User:"+home.getEnterpriseBranchId(),
//                "AudioConfiguration:"+home.getEnterpriseBranchId(),"DispenserConfiguration:"+home.getEnterpriseBranchId(),"QueueConfiguration:"+home.getEnterpriseBranchId(),
//                "ThemeConfiguration:"+home.getEnterpriseBranchId(),"TokenConfiguration:"+home.getEnterpriseBranchId(),"User:"+home.getEnterpriseBranchId(),
//                "SmsConfiguration:"+home.getEnterpriseBranchId(), "Branch:"+home.getEnterpriseBranchId(),"Survey:"+home.getEnterpriseBranchId(),"SurveyConfiguration:"+home.getEnterpriseBranchId(),
//                "Appointment:"+home.getEnterpriseBranchId(), "AppointmentConfiguration:"+home.getEnterpriseBranchId(), "MeetingConfiguration:"+home.getEnterpriseBranchId(),"KpiConfiguration:"+home.getEnterpriseBranchId(),
//                "QRToken:"+home.getEnterpriseBranchId(),"BreakConfiguration:"+home.getEnterpriseBranchId()
//
//        };
//        container.setQueueNames(queueNames);
//        return container;
//    }
//}
