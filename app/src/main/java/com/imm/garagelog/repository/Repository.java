package com.imm.garagelog.repository;

import android.content.Context;
import android.widget.Toast;

import com.imm.garagelog.activities.MainActivity;
import com.imm.garagelog.domain.Car;
import com.imm.garagelog.utils.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ionut on 30/10/2017.
 */

public class Repository extends Observer implements Serializable {
    private List<Observer> observers = new ArrayList<Observer>();
    private List<Car> carList;

    public Repository() {
        carList = new ArrayList<>();
        this.attach(this);
    }

    public void addCar(Car car) {
        car.setId(getNextId());
        carList.add(car);
        notifyAllObservers();
    }

    public void deleteCar(int id) {
        carList.remove(id);
        //Repair IDs
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).setId(i + 1);
        }
        notifyAllObservers();
    }

    public void updateCar(int index, Car car) {
        carList.get(index).setBrand(car.getBrand());
        carList.get(index).setBrandLogoUrl(car.getBrandLogoUrl());
        carList.get(index).setEngineSize(car.getEngineSize());
        carList.get(index).setModel(car.getModel());
        notifyAllObservers();
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

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }

    @Override
    public void update() {
        MainActivity.showRepositoryUpdated();
    }
}
