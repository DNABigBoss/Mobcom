package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 11/10/2020.
 */
public class StepResepData {
    @SerializedName("info")
    public List<DatumInfo> info;
    
    @SerializedName("bahan")
    public List<DatumBahan> bahan;

    @SerializedName("step")
    public   List<DatumStep> step;

    public StepResepData(List<DatumInfo> info, List<DatumBahan> bahan, List<DatumStep> step) {
        this.info = info;
        this.bahan = bahan;
        this.step = step;
    }

    public List<DatumInfo> getInfo() {
        return info;
    }

    public void setInfo(List<DatumInfo> info) {
        this.info = info;
    }

    public List<DatumBahan> getBahan() {
        return bahan;
    }

    public void setBahan(List<DatumBahan> bahan) {
        this.bahan = bahan;
    }

    public List<DatumStep> getStep() {
        return step;
    }

    public void setStep(List<DatumStep> step) {
        this.step = step;
    }

    public class DatumBahan {
        @SerializedName("nama")
        public String nama;
        @SerializedName("takaran")
        public String takaran;

        public DatumBahan(String nama, String takaran) {
            this.nama = nama;
            this.takaran = takaran;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getTakaran() {
            return takaran;
        }

        public void setTakaran(String takaran) {
            this.takaran = takaran;
        }
    }

    public class DatumStep {
        @SerializedName("nomor_step")
        public Integer nomor_step;
        @SerializedName("intruksi")
        public String intruksi;

        public DatumStep(Integer nomor_step, String intruksi) {
            this.nomor_step = nomor_step;
            this.intruksi = intruksi;
        }

        public Integer getNomor_step() {
            return nomor_step;
        }

        public void setNomor_step(Integer nomor_step) {
            this.nomor_step = nomor_step;
        }

        public String getIntruksi() {
            return intruksi;
        }

        public void setIntruksi(String intruksi) {
            this.intruksi = intruksi;
        }
    }

    public class DatumInfo {
        @SerializedName("id")
        public Integer id;
        @SerializedName("nama")
        public String nama;
        @SerializedName("waktu_memasak")
        public String waktu_memasak;
        @SerializedName("porsi")
        public Integer porsi;
        @SerializedName("harga")
        public Double harga;
        @SerializedName("favorit")
        public Integer favorit;
        @SerializedName("dilihat")
        public Integer dilihat;
        @SerializedName("gambar")
        public String gambar;

        public DatumInfo(Integer id, String nama, String waktu_memasak, Integer porsi, Double harga, Integer favorit, Integer dilihat, String gambar) {
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

}
