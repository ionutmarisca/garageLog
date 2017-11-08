package com.imm.garagelog.repository;

import com.imm.garagelog.domain.Car;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ionut on 30/10/2017.
 */

public class Repository implements Serializable {
    private List<Car> carList;

    public Repository() {
        carList = new ArrayList<>();
    }

    public void addCar(Car car) {
        carList.add(car);
    }

    public void updateCar(int index, Car car) {
        carList.get(index).setBrand(car.getBrand());
        carList.get(index).setBrandLogoUrl(car.getBrandLogoUrl());
        carList.get(index).setEngineSize(car.getEngineSize());
        carList.get(index).setModel(car.getModel());
    }

    public Car getCar(int index) {
        return carList.get(index);
    }

    public List<Car> getCarList() {
        return this.carList;
    }
}
