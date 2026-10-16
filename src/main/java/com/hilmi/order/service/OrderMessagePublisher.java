package com.hilmi.order.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hilmi.order.config.RabbitMQConfig;
import com.hilmi.order.vo.OrderMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderMessagePublisher {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  public void publishOrder(OrderMessage orderMessage) {
    try {
      String payload = objectMapper.writeValueAsString(orderMessage);
      rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE_NAME, payload);
      System.out.println("Order sent to queue: " + payload);
    } catch (Exception e) {
      throw new RuntimeException("Failed to publish order message", e);
    }
  }
}
