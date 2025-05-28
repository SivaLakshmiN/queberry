package io.queberry.que.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@Profile("enterprise")
@RequiredArgsConstructor

public class RabbitMQConfig {

//    private final Home home;
//
//    private final Environment environment;
//
//    private final ObjectMapper objectMapper;
//
//    @Bean
//    public DefaultClassMapper classMapper() {
//        DefaultClassMapper classMapper = new DefaultClassMapper();
//        Map<String, Class<?>> idClassMapping = new HashMap<>();
//        idClassMapping.put("io.cloudloom.que.enterprise.service.Service", Service.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.template.Template", Template.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.ticker.Ticker", Ticker.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.media.Playlist", Playlist.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.counter.Counter", CounterResource.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.device.Device", DeviceResource.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.media.Media", Media.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.auth.Role", Role.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.service.ServiceGroup", ServiceGroupResource.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.user.User", EmployeeResource.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.theme.ThemeConfiguration", ThemeConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.audio.AudioConfiguration", AudioConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.dispenser.DispenserConfiguration", DispenserConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.queue.QueueConfiguration", QueueConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.token.TokenConfiguration", TokenConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.sms.SmsConfiguration", SmsConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.survey.SurveyConfiguration", SurveyConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.branch.Branch", Home.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.feedback.survey.Survey", Survey.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.appointment.Appointment", Appointment.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.appointment.AppointmentConfiguration", AppointmentConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.meeting.MeetingConfiguration", MeetingConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.kpi.KpiConfiguration",KpiConfiguration.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.qrcode.QRToken", QRToken.class);
//        idClassMapping.put("io.cloudloom.que.enterprise.configuration.breakConfig.BreakConfiguration", BreakConfiguration.class);
//        classMapper.setIdClassMapping(idClassMapping);
//        return classMapper;
//    }
//
//    /*@Bean
//    public DirectExchange assistanceExchange(){
//        //Declare Exchange,Queue and Binding for Assistance
//        DirectExchange assistanceExchange = new DirectExchange("Assistance");
//        Queue assistanceQueue = new Queue("Assistance");
//        Binding assisanceBinding = bind(assistanceQueue).to(assistanceExchange).with("");
//        RabbitAdmin amqpAdmin = new RabbitAdmin(connectionFactory());
//        amqpAdmin.declareExchange(assistanceExchange);
//        amqpAdmin.declareQueue(assistanceQueue);
//        amqpAdmin.declareBinding(assisanceBinding);
//        return assistanceExchange;
//    }*/
//
//    @Bean
//    public MessageConverter jsonMessageConverter(){
//        objectMapper.registerModule(new JavaTimeModule());
//        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
//        messageConverter.setClassMapper(classMapper());
//        return messageConverter;
//    }
//
//    @Bean
//    @Lazy
//    public ConnectionFactory connectionFactory() throws KeyManagementException, NoSuchAlgorithmException {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost(home.getEnterpriseURL().replace("https","").replace("http","").replace("://","").split(":")[0]);
//        connectionFactory.setUsername(home.getRabbitMqUsername());
//        connectionFactory.setPassword(home.getRabbitMqpassword());
//        connectionFactory.setPort(5672);
//        if (Arrays.asList(environment.getActiveProfiles()).contains("invest")){
//            connectionFactory.setPort(5671);
//            connectionFactory.getRabbitConnectionFactory().useSslProtocol();
//        }
//        if (home.getEnterpriseURL().equals("1.1.1.1")){
//            connectionFactory.setCloseTimeout(100);
//            connectionFactory.setConnectionTimeout(100);
//            connectionFactory.setChannelCheckoutTimeout(100);
//        }
//        return connectionFactory;
//    }

    /*@Bean
    public SimpleMessageListenerContainer container() throws NoSuchAlgorithmException, KeyManagementException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        MessageListenerAdapter messageListenerAdapter = new  MessageListenerAdapter(new AggregateRootListener(aggregateRootService), "handleMessage");
        messageListenerAdapter.setMessageConverter(jsonMessageConverter());
        container.setMessageListener(messageListenerAdapter);
        String[] queueNames = {"Service:"+home.getEnterpriseBranchId(), "Counter:"+home.getEnterpriseBranchId(),"Device:"+home.getEnterpriseBranchId(), "Events:"+home.getEnterpriseBranchId(),"Media:"+home.getEnterpriseBranchId(),
                "Playlist:"+home.getEnterpriseBranchId(),"Role:"+home.getEnterpriseBranchId(),"ServiceGroup:"+home.getEnterpriseBranchId(),
                "Template:"+home.getEnterpriseBranchId(),"Ticker:"+home.getEnterpriseBranchId(),"User:"+home.getEnterpriseBranchId(),
                "AudioConfiguration:"+home.getEnterpriseBranchId(),"DispenserConfiguration:"+home.getEnterpriseBranchId(),"QueueConfiguration:"+home.getEnterpriseBranchId(),
                "ThemeConfiguration:"+home.getEnterpriseBranchId(),"TokenConfiguration:"+home.getEnterpriseBranchId(),"User:"+home.getEnterpriseBranchId(),
                "SmsConfiguration:"+home.getEnterpriseBranchId(), "Branch:"+home.getEnterpriseBranchId(),"Survey:"+home.getEnterpriseBranchId(),"SurveyConfiguration:"+home.getEnterpriseBranchId(),
                "Appointment:"+home.getEnterpriseBranchId()
        };
        container.setQueueNames(queueNames);
        return container;
    }*/
}
