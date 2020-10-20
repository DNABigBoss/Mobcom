package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

public class PencarianResep {
    @SerializedName("id") private int id;
    @SerializedName("nama") private String nama;
    @SerializedName("gambar") private static String gambar;

    public PencarianResep(int id, String nama, String gambar){
        this.id = id;
        this.nama = nama;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
