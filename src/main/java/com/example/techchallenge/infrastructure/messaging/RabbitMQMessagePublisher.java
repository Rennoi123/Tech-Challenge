package com.example.techchallenge.infrastructure.messaging;

import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.interfaces.IMessagePublisher;
import com.example.techchallenge.infrastructure.configuration.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessagePublisher implements IMessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQMessagePublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderCreated(Order order) {
        try {
            logger.info("📤 Publicando evento 'pedido.criado' para Order ID: {}", order.getId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_PEDIDO, RabbitMQConfig.ROUTING_KEY_CRIADO, order, message -> {
                message.getMessageProperties().setHeader("eventType", "pedido.criado");
                return message;
            });
        } catch (Exception e) {
            System.err.println("Falha ao publicar evento 'pedido.criado' para Order ID: " + order.getId() + ". Erro: " + e.getMessage());
        }
    }

    @Override
    public void publishOrderCompleted(Order order) {
        try {
            logger.info("📤 Publicando evento 'pedido.finalizado' para Order ID: {}", order.getId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_PEDIDO, RabbitMQConfig.ROUTING_KEY_FINALIZADO, order, message -> {
                message.getMessageProperties().setHeader("eventType", "pedido.finalizado");
                return message;
            });
        } catch (Exception e) {
            System.err.println("Falha ao publicar evento 'pedido.finalizado' para Order ID: " + order.getId() + ". Erro: " + e.getMessage());
        }
    }
}
