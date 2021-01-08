package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 28/12/2020.
 */
public class ResepUserData {
    @SerializedName("id")
    private Integer id;
    @SerializedName("id_users")
    private String id_users;
    @SerializedName("nama_resep")
    private String nama_resep;
    @SerializedName("waktu_memasak")
    private String waktu_memasak;
    @SerializedName("porsi")
    private String porsi;
    @SerializedName("harga")
    private Double harga;
    @SerializedName("favorit")
    private Integer favorit;
    @SerializedName("dilihat")
    private Integer dilihat;
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("is_approve")
    private String is_approve;
    @SerializedName("id_approve")
    private String id_approve;
    @SerializedName("is_migrated")
    private String is_migrated;
    @SerializedName("tanggal_update")
    private String tangal_update;
    @SerializedName("bahan")
    public List<DatumBahan> bahan;
    @SerializedName("step")
    public List<DatumStep> step;

    public ResepUserData(Integer id, String nama_resep, String waktu_memasak, String porsi, Double harga, Integer favorit, Integer dilihat, String gambar, String id_users, String is_approve, String id_approve, String is_migrated, String tangal_update, List<DatumBahan> bahan, List<DatumStep> step) {
        this.id = id;
        this.nama_resep = nama_resep;
        this.waktu_memasak = waktu_memasak;
        this.porsi = porsi;
        this.harga = harga;
        this.favorit = favorit;
        this.dilihat = dilihat;
        this.gambar = gambar;
        this.id_users = id_users;
        this.is_approve = is_approve;
        this.id_approve = id_approve;
        this.is_migrated = is_migrated;
        this.tangal_update = tangal_update;
        this.bahan = bahan;
        this.step = step;
    }

    public String getnama_resep() {
        return nama_resep;
    }

    public void setnama_resep(String nama_resep) {
        this.nama_resep = nama_resep;
    }

    public String getWaktu_memasak() {
        return waktu_memasak;
    }

    public void setWaktu_memasak(String waktu_memasak) {
        this.waktu_memasak = waktu_memasak;
    }

    public String getPorsi() {
        return porsi;
    }

    public void setPorsi(String porsi) {
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
        return "http://resepjajanankekinian.my.id/assets/img/users/resep/"+gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    public String getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(String is_approve) {
        this.is_approve = is_approve;
    }

    public String getId_approve() {
        return id_approve;
    }

    public void setId_approve(String id_approve) {
        this.id_approve = id_approve;
    }

    public String getIs_migrated() {
        return is_migrated;
    }

    public void setIs_migrated(String is_migrated) {
        this.is_migrated = is_migrated;
    }

    public String getTangal_update() {
        return tangal_update;
    }

    public void setTangal_update(String tangal_update) {
        this.tangal_update = tangal_update;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public class DatumBahan {
        @SerializedName("id")
        public Integer id;
        @SerializedName("nama_bahan")
        public String nama_bahan;
        @SerializedName("takaran")
        public String takaran;
        @SerializedName("is_approve")
        public String is_approve;

        public DatumBahan(Integer id, String nama_bahan, String takaran, String is_approve) {
            this.id = id;
            this.nama_bahan = nama_bahan;
            this.takaran = takaran;
            this.is_approve = is_approve;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getIs_approve() {
            return is_approve;
        }

        public void setIs_approve(String is_approve) {
            this.is_approve = is_approve;
        }

        public String getNama_bahan() {
            return nama_bahan;
        }

        public void setNama_bahan(String nama_bahan) {
            this.nama_bahan = nama_bahan;
        }

        public String getTakaran() {
            return takaran;
        }

        public void setTakaran(String takaran) {
            this.takaran = takaran;
        }
    }

    public class DatumStep {
        @SerializedName("id")
        public Integer id;
        @SerializedName("nomor_step")
        public Integer nomor_step;
        @SerializedName("intruksi")
        public String intruksi;

        public DatumStep(Integer id, Integer nomor_step, String intruksi) {
            this.id = id;
            this.nomor_step = nomor_step;
            this.intruksi = intruksi;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

}
