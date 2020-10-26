package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 25/10/2020.
 */
public class DiskusiData {
    @SerializedName("id")
    public Integer id;
    @SerializedName("isi")
    public String isi;
    @SerializedName("user_id")
    public Integer user_id;
    @SerializedName("nama")
    public String nama;
    @SerializedName("disukai")
    public Integer disukai;
    @SerializedName("tanggal")
    public String tanggal;

    public DiskusiData(Integer id, String isi, Integer user_id, String nama, Integer disukai, String tanggal) {
        this.id = id;
        this.isi = isi;
        this.user_id = user_id;
        this.nama = nama;
        this.disukai = disukai;
        this.tanggal = tanggal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getDisukai() {
        return disukai;
    }

    public void setDisukai(Integer disukai) {
        this.disukai = disukai;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
