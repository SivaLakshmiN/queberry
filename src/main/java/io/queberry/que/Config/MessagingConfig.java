package io.queberry.que.Config;//package io.queberry.que.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//

//@Slf4j
//@Configuration
//@EnableConfigurationProperties({KafkaProperties.class})
//public class MessagingConfig {
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final KafkaProperties properties;
//
//    public MessagingConfig(KafkaProperties properties) {
//        this.properties = properties;
//    }
//
//    @Bean
//    public JsonSerializer jsonSerializer(){
//        JsonSerializer jsonSerializer = new JsonSerializer(objectMapper);
//        return jsonSerializer;
//    }
//
//    @Bean
//    public ProducerFactory<?, ?> producerFactory() {
//        DefaultKafkaProducerFactory<?, ?> producerFactory =
//                new DefaultKafkaProducerFactory(this.properties.buildProducerProperties());
//        producerFactory.setValueSerializer(jsonSerializer());
//        return producerFactory;
//    }
//}