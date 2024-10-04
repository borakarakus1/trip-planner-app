package trip_planner_app.shared_service.messaging;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Her bir mesaj türü için işleme fonksiyonlarını sağlayan genel interface.
 * Bu interface, farklı mesaj türleri için uygulanabilir.
 */
@FunctionalInterface
public interface MessageHandler {
    /**
     * Mesajın işlenmesini sağlayan fonksiyon.
     *
     * @param message Gelen mesaj
     */
    void handle(JsonNode message);
}
