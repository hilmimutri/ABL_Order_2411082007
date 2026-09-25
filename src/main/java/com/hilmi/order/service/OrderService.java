package com.hilmi.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hilmi.order.entity.Order;
import com.hilmi.order.repository.OrderRepository;
import com.hilmi.order.vo.Pelanggan;
import com.hilmi.order.vo.Produk;
import com.hilmi.order.vo.ResponseTemplate;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate = new RestTemplate();

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // public Order updateOrder(Long id, Order order) {
    //     Order existingOrder = orderRepository.findById(id).orElse(null);
    //     if (existingOrder != null) {
    //         existingOrder.setProduk_id(order.getProduk_id());
    //         existingOrder.setPelanggan_id(order.getPelanggan_id());
    //         existingOrder.setTgl_Trans(order.getTgl_Trans());
    //         existingOrder.setJumlah(order.getJumlah());
    //         existingOrder.setTotal(order.getTotal());
    //         return orderRepository.save(existingOrder);
    //     }
    //     return null;
    // }

    public ResponseTemplate getOrderWithProdukById(Long id) {
        Order order = getOrderById(id);
        if (order == null) {
            return null;
        }

        ServiceInstance serviceInstanceProduk = discoveryClient.getInstances("PRODUK").get(0);
        ServiceInstance serviceInstancePelanggan = discoveryClient.getInstances("PELANGGAN").get(0);

        Produk produk = restTemplate.getForObject(
                serviceInstanceProduk.getUri() + "/api/produk/" + order.getProdukId(),
                Produk.class);

        Pelanggan pelanggan = restTemplate.getForObject(
                serviceInstancePelanggan.getUri() + "/api/pelanggan/" + order.getPelangganId(),
                Pelanggan.class);

        ResponseTemplate vo = new ResponseTemplate();
        vo.setOrder(order);
        vo.setProduk(produk);
        vo.setPelanggan(pelanggan);
        return vo;
    }
}