package trip_planner_app.shared_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue sharedQueue() {
        return new Queue(rabbitMQProperties.getQueueName(), true);
    }

    @Bean
    public TopicExchange sharedExchange() {
        return new TopicExchange(rabbitMQProperties.getExchangeName());
    }

    @Bean
    public Binding bindingSharedQueue(Queue sharedQueue, TopicExchange sharedExchange) {
        return BindingBuilder.bind(sharedQueue).to(sharedExchange).with(rabbitMQProperties.getRoutingKey());
    }
}
