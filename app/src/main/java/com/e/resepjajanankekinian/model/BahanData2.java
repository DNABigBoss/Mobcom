package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

public class BahanData2 {
    @SerializedName("id")
    private Integer id;

    @SerializedName("nama")
    private String nama;

    public BahanData2(Integer id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public Integer getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

}
