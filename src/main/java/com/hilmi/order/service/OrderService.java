package com.hilmi.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hilmi.order.entity.Order;
import com.hilmi.order.repository.OrderRepository;
import com.hilmi.order.vo.OrderMessage;
import com.hilmi.order.vo.Pelanggan;
import com.hilmi.order.vo.Produk;
import com.hilmi.order.vo.ResponseTemplate;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DiscoveryClient discoveryClient;

  @Autowired
  private OrderMessagePublisher orderMessagePublisher;

  private RestTemplate restTemplate = new RestTemplate();

  public Order getOrderById(Long id) {
    return orderRepository.findById(id).orElse(null);
  }

  public List<Order> getAllOrder() {
    return orderRepository.findAll();
  }

  public Order saveOrder(Order order) {
    if (order.getTglTransaksi() == null) {
      order.setTglTransaksi(LocalDate.now());
    }
    Order saved = orderRepository.save(order);
    Pelanggan pelanggan = fetchPelanggan(saved.getPelangganId());
    orderMessagePublisher.publishOrder(new OrderMessage(saved, pelanggan.getEmail()));
    return saved;
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

  public List<ResponseTemplate> getOrderWithProdukById(Long id) {
    List<ResponseTemplate> responseList = new ArrayList<>();
    Order order = getOrderById(id);

    Produk produk = fetchProduk(order.getProdukId());
    Pelanggan pelanggan = fetchPelanggan(order.getPelangganId());

    ResponseTemplate vo = new ResponseTemplate();
    vo.setOrder(order);
    vo.setProduk(produk);
    vo.setPelanggan(pelanggan);
    responseList.add(vo);
    return responseList;
  }

  private Produk fetchProduk(Long produkId) {
    ServiceInstance instance = discoveryClient.getInstances("PRODUK").get(0);
    return restTemplate.getForObject(instance.getUri() + "/api/produk/" + produkId, Produk.class);
  }

  private Pelanggan fetchPelanggan(Long pelangganId) {
    ServiceInstance instance = discoveryClient.getInstances("PELANGGAN").get(0);
    return restTemplate.getForObject(instance.getUri() + "/api/pelanggan/" + pelangganId, Pelanggan.class);
  }
}
