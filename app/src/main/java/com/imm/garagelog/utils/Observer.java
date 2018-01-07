package com.imm.garagelog.utils;

import com.imm.garagelog.repository.Repository;

/**
 * Created by Ionut on 7/1/2018.
 */

public abstract class Observer {
    protected Repository repository;
    public abstract void update();
}