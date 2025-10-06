package com.example.techchallenge.infrastructure.messaging;

import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.infrastructure.configuration.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDO)
    public void consumingEvent(Order order, @Header(name = "eventType", required = false) String eventType) {
        if (eventType == null) {
            eventType = "evento.desconhecido";
        }

        if ("pedido.criado".equalsIgnoreCase(eventType)) {
            logger.info("Pedido criado: {}", order.getId());
        } else if ("pedido.finalizado".equalsIgnoreCase(eventType)) {
            logger.info("Pedido finalizado: {}", order.getId());
        } else {
            logger.info("Evento desconhecido: {}", eventType);
        }
    }
}
