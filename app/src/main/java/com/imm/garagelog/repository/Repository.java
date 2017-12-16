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
        car.setId(getNextId());
        carList.add(car);
    }

    public void deleteCar(int id) {
        carList.remove(id);
        //Repair IDs
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).setId(i + 1);
        }
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

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    private int getNextId() {
        if (carList.size() == 0) {
            return 1;
        } else {
            return carList.size() + 1;
        }
    }
}
