package trip_planner_app.shared_service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final GenericMessageProcessor messageProcessor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private  Map<String, MessageHandler> messageHandlers;
    private final List<String> receivedMessages = new ArrayList<>();

    public void GenericMessageProcessor(Map<String, MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    /**
     * Mesajı işleyen merkezi fonksiyon. Mesajın türünü belirler ve ilgili işlemi gerçekleştirir.
     *
     * @param message Gelen mesaj içeriği
     */
    public void processMessage(String message) {
        try {
            receivedMessages.add(message);
            JsonNode jsonNode = objectMapper.readTree(message);

            String eventType = jsonNode.get("eventType").asText();

            MessageHandler handler = messageHandlers.get(eventType);
            if (handler != null) {
                handler.handle(jsonNode);
            } else {
                System.out.println("No handler found for event type: " + eventType);
            }

        } catch (IOException e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }

    /**
     * Alınan tüm mesajları döner.
     *
     * @return List of received messages
     */
    public List<String> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    /**
     * RabbitMQ'ya mesaj gönderir.
     *
     * @param exchange   Mesajın gönderileceği exchange adı
     * @param routingKey Mesajın yönlendirileceği routing key
     * @param message    Gönderilecek mesaj içeriği
     */
    public void sendMessage(String exchange, String routingKey, Object message) {
        try {
            String messageAsJson = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(exchange, routingKey, messageAsJson);
            System.out.println("Message sent to RabbitMQ: " + messageAsJson);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert message to JSON: " + e.getMessage());
        }
    }

    /**
     * RabbitMQ kuyruğundan gelen mesajları dinler ve işlenmesi için GenericMessageProcessor'a iletir.
     *
     * @param message Gelen mesaj içeriği (JSON formatında)
     */

    @RabbitListener(queues = RabbitMQConstants.SHARED_QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
        messageProcessor.processMessage(message);
    }
}

