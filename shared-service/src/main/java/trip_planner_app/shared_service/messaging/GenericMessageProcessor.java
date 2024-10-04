package trip_planner_app.shared_service.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GenericMessageProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, MessageHandler> messageHandlers = new HashMap<>();

    public GenericMessageProcessor() {
        messageHandlers.put("UserCreated", this::handleUserCreated);
        messageHandlers.put("OrderPlaced", this::handleOrderPlaced);
        messageHandlers.put("PaymentProcessed", this::handlePaymentProcessed);
    }

    /**
     * Mesajı işleyen merkezi fonksiyon. Mesajın türünü belirler ve ilgili işlemi gerçekleştirir.
     *
     * @param message Gelen mesaj içeriği
     */
    public void processMessage(String message) {
        try {
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
     * UserCreated mesajlarını işleyen fonksiyon.
     *
     * @param jsonNode Mesaj içeriği
     */
    private void handleUserCreated(JsonNode jsonNode) {
        String username = jsonNode.get("payload").get("username").asText();
        String email = jsonNode.get("payload").get("email").asText();
        System.out.println("Handling UserCreated event for user: " + username + ", email: " + email);
    }

    /**
     * OrderPlaced mesajlarını işleyen fonksiyon.
     *
     * @param jsonNode Mesaj içeriği
     */
    private void handleOrderPlaced(JsonNode jsonNode) {
        String orderId = jsonNode.get("payload").get("orderId").asText();
        String product = jsonNode.get("payload").get("product").asText();
        System.out.println("Handling OrderPlaced event for order: " + orderId + ", product: " + product);
    }

    /**
     * PaymentProcessed mesajlarını işleyen fonksiyon.
     *
     * @param jsonNode Mesaj içeriği
     */
    private void handlePaymentProcessed(JsonNode jsonNode) {
        String paymentId = jsonNode.get("payload").get("paymentId").asText();
        String amount = jsonNode.get("payload").get("amount").asText();
        System.out.println("Handling PaymentProcessed event for paymentId: " + paymentId + ", amount: " + amount);
    }
}
