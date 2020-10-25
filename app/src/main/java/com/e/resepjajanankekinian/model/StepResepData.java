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
    public List<DatumStep> step;

    @SerializedName("diskusi")
    public List<DatumDiskusi> diskusi;

    public StepResepData(List<DatumInfo> info, List<DatumBahan> bahan, List<DatumStep> step, List<DatumDiskusi> diskusi) {
        this.info = info;
        this.bahan = bahan;
        this.step = step;
        this.diskusi = diskusi;
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

    public List<DatumDiskusi> getDiskusi() {
        return diskusi;
    }

    public void setDiskusi(List<DatumDiskusi> diskusi) {
        this.diskusi = diskusi;
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

    public class DatumDiskusi {
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

        public DatumDiskusi(Integer id, String isi, Integer user_id, String nama, Integer disukai, String tanggal) {
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

}
