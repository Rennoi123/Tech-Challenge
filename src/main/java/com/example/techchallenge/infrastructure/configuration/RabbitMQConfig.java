package com.example.techchallenge.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PEDIDO = "pedido.queue";
    public static final String EXCHANGE_PEDIDO = "pedido.exchange";
    public static final String ROUTING_KEY_CRIADO = "pedido.criado";
    public static final String ROUTING_KEY_FINALIZADO = "pedido.finalizado";

    @Bean
    public Queue pedidoQueue() {
        return new Queue(QUEUE_PEDIDO, true);
    }

    @Bean
    public TopicExchange pedidoExchange() {
        return new TopicExchange(EXCHANGE_PEDIDO);
    }

    @Bean
    public Binding bindingCriado(Queue pedidoQueue, TopicExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoQueue).to(pedidoExchange).with(ROUTING_KEY_CRIADO);
    }

    @Bean
    public Binding bindingFinalizado(Queue pedidoQueue, TopicExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoQueue).to(pedidoExchange).with(ROUTING_KEY_FINALIZADO);
    }
}
