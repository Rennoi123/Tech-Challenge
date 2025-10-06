package com.example.techchallenge.infrastructure.configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PEDIDO = "pedido.queue";
    public static final String EXCHANGE_PEDIDO = "pedido.exchange";
    public static final String ROUTING_KEY_CRIADO = "pedido.criado";
    public static final String ROUTING_KEY_FINALIZADO = "pedido.finalizado";

    @Bean
    public Queue orderQueue() {
        return new Queue(QUEUE_PEDIDO, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_PEDIDO);
    }

    @Bean
    public Binding bindingOrderCreated(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ROUTING_KEY_CRIADO);
    }

    @Bean
    public Binding bindingOrderCompleted(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ROUTING_KEY_FINALIZADO);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        template.setMandatory(true);

        template.setConfirmCallback((correlation, ack, cause) -> {
            if (ack) {
                System.out.println("Broker ACK para correlationId=" + (correlation != null ? correlation.getId() : null));
            } else {
                System.err.println("Broker NACK: " + cause);
            }
        });

        template.setReturnsCallback(ret -> {
            System.err.printf("RETURN: exchange=%s rk=%s replyCode=%d replyText=%s%n",
                    ret.getExchange(), ret.getRoutingKey(), ret.getReplyCode(), ret.getReplyText());
        });

        return template;
    }
}
