package com.imm.garagelog.domain;

import java.io.Serializable;

/**
 * Created by Ionut on 30/10/2017.
 */

public class Car implements Serializable {
    private int id;
    private String brand;
    private String model;
    private String brandLogoUrl;
    private int engineSize;

    public Car() {
    }

    public Car(int id, String brand, String model, String brandLogoUrl, int engineSize) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.brandLogoUrl = brandLogoUrl;
        this.engineSize = engineSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public int getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(int engineSize) {
        this.engineSize = engineSize;
    }
}
