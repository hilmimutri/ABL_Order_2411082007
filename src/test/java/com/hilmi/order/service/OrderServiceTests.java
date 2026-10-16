package com.hilmi.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.hilmi.order.entity.Order;
import com.hilmi.order.repository.OrderRepository;
import com.hilmi.order.vo.OrderMessage;
import com.hilmi.order.vo.Pelanggan;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DiscoveryClient discoveryClient;

    @Mock
    private OrderMessagePublisher orderMessagePublisher;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ServiceInstance pelangganInstance;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderService, "restTemplate", restTemplate);
    }

    @Test
    void saveOrderSetsDateAndPublishesReferencePayload() {
        Order order = new Order(null, 10L, 20L, null, 2, 5000);
        Pelanggan pelanggan = new Pelanggan(20L, "Ahsan", "ahsan@example.com", "Alamat", "L");
        when(orderRepository.save(order)).thenReturn(order);
        when(discoveryClient.getInstances("PELANGGAN")).thenReturn(List.of(pelangganInstance));
        when(pelangganInstance.getUri()).thenReturn(URI.create("http://localhost:8082"));
        when(restTemplate.getForObject(eq("http://localhost:8082/api/pelanggan/20"), eq(Pelanggan.class)))
                .thenReturn(pelanggan);

        orderService.saveOrder(order);

        assertThat(order.getTglTransaksi()).isEqualTo(LocalDate.now());
        ArgumentCaptor<OrderMessage> message = ArgumentCaptor.forClass(OrderMessage.class);
        verify(orderMessagePublisher).publishOrder(message.capture());
        assertThat(message.getValue().getEmail()).isEqualTo("ahsan@example.com");
        assertThat(message.getValue().getTglTransaksi()).isEqualTo(LocalDate.now());
    }
}
