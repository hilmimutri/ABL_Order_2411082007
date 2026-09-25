package com.hilmi.order.vo;

import com.hilmi.order.entity.Order;

public class ResponseTemplate {
    private Produk produk;
    private Order order;
    private Pelanggan pelanggan;

    public ResponseTemplate() {

    }

    public ResponseTemplate(Produk produk, Order order, Pelanggan pelanggan) {
        this.produk = produk;
        this.order = order;
        this.pelanggan = pelanggan;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }
}