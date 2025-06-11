package io.queberry.que.config.Websocket;
import io.queberry.que.device.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketOperations {

    private SimpMessageSendingOperations simpMessageSendingOperations;

    public WebSocketOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void send(String topic, Object object) {
        log.info("Sending {} to {}", object, topic);
        simpMessageSendingOperations.convertAndSend(topic, object);
    }

    public void sendToDevice(Device device, String topic, Object object) {
        log.info("Sending {} to user {}", object, device.getDeviceId());
        simpMessageSendingOperations.convertAndSendToUser(device.getDeviceId(), topic, object);
    }

    public void sendToDevices(Collection<Device> devices, String topic, Object object) {
        devices.parallelStream().forEach(device -> sendToDevice(device, topic, object));
    }
}

