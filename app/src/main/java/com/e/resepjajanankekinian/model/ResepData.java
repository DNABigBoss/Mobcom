package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 10/10/2020.
 */
public class ResepData {
    @SerializedName("id")
    private Integer id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("waktu_memasak")
    private String waktu_memasak;
    @SerializedName("porsi")
    private Integer porsi;
    @SerializedName("harga")
    private Double harga;
    @SerializedName("favorit")
    private Integer favorit;
    @SerializedName("dilihat")
    private Integer dilihat;
    @SerializedName("gambar")
    private String gambar;

    public ResepData(Integer id, String nama, String waktu_memasak, Integer porsi, Double harga, Integer favorit, Integer dilihat, String gambar) {
        this.id = id;
        this.nama = nama;
        this.waktu_memasak = waktu_memasak;
        this.porsi = porsi;
        this.harga = harga;
        this.favorit = favorit;
        this.dilihat = dilihat;
        this.gambar = gambar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu_memasak() {
        return waktu_memasak;
    }

    public void setWaktu_memasak(String waktu_memasak) {
        this.waktu_memasak = waktu_memasak;
    }

    public Integer getPorsi() {
        return porsi;
    }

    public void setPorsi(Integer porsi) {
        this.porsi = porsi;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public Integer getFavorit() {
        return favorit;
    }

    public void setFavorit(Integer favorit) {
        this.favorit = favorit;
    }

    public Integer getDilihat() {
        return dilihat;
    }

    public void setDilihat(Integer dilihat) {
        this.dilihat = dilihat;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
