package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Resep Jajanan Kekinian on 17/10/2020.
 */
public class UserData {
    @SerializedName("id")
    private Integer id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("email")
    private String email;
    @SerializedName("pass")
    private String pass;
    @SerializedName("foto")
    private String foto;

    public UserData(Integer id, String nama, String email, String pass, String foto) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.pass = pass;
        this.foto = foto;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFoto() {
        return "https://resepjajanankekinian.my.id/assets/img/users/"+foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
