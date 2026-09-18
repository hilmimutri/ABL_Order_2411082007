package com.hilmi.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hilmi.order.entity.Order;
import com.hilmi.order.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired 
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            existingOrder.setProdukId(order.getProdukId());
            existingOrder.setPelangganId(order.getPelangganId());
            existingOrder.setTglTransaksi(order.getTglTransaksi());
            existingOrder.setJumlah(order.getJumlah());
            existingOrder.setTotal(order.getTotal());
            return orderRepository.save(existingOrder);
        }
        return null;
    }
}