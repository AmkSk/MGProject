package com.mg.objects;

import java.util.ArrayList;
import java.util.List;

public class Building extends MGEntry{

    // =============================================================================
    // Fields
    // =============================================================================

    private String address = null;
    private int color;
    private String gps = null;

    // =============================================================================
    // Constructors
    // =============================================================================

    public Building(){}

    public Building(String name){
        this.setName(name);
    }
    // =============================================================================
    // Getters
    // =============================================================================

    public String getAddress() {
        return address;
    }

    public int getColor() {
        return color;
    }

    public String getGps() {
        return gps;
    }

    // =============================================================================
    // Setters
    // =============================================================================

    public void setAddress(String address) {
        this.address = address;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    // =============================================================================
    // Methods
    // =============================================================================


}
