package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 12/10/2020.
 */
public class BahanData {
    @SerializedName("id")
    private Integer id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("gambar")
    private String gambar;

    public BahanData(Integer id, String nama, String gambar) {
        this.id = id;
        this.nama = nama;
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

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
