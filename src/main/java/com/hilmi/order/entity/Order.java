package com.hilmi.order.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long produk_Id;
    private Long pelanggan_Id;
    private LocalDate tgl_Trans;
    private Integer jumlah;
    private Double total;

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdukId() {
        return produk_Id;
    }

    public void setProdukId(Long produk_Id) {
        this.produk_Id = produk_Id;
    }

    public Long getPelangganId() {
        return pelanggan_Id;
    }

    public void setPelangganId(Long pelanggan_Id) {
        this.pelanggan_Id = pelanggan_Id;
    }

    public LocalDate getTglTransaksi() {
        return tgl_Trans;
    }

    public void setTglTransaksi(LocalDate tgl_Trans) {
        this.tgl_Trans = tgl_Trans;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}