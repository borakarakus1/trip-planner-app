package trip_planner_app.shared_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class RabbitMQProperties {

    @Value("${shared.rabbitmq.queue-name}")
    private String queueName;

    @Value("${shared.rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${shared.rabbitmq.routing-key}")
    private String routingKey;

    public String getQueueName() {
        return queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}

