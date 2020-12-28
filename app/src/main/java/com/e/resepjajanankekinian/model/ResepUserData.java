package com.e.resepjajanankekinian.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 28/12/2020.
 */
public class ResepUserData {
    @SerializedName("id")
    private Integer id;

    public ResepUserData(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
