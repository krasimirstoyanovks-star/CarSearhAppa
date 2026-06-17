package com.example.carsearhapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Serializable {

    private String vin;
    private String make;
    private String model;
    private String year;
    private String engine;
    private String fuel;
    

    private List<ServiceRecord> serviceHistory = new ArrayList<>();
    private String insuranceExpiry;
    private String nextOilChange;
    private String nextOilChangeMileage;
    private String technicalInspectionExpiry;
    private String averageConsumption;
    private String lastFuelAmount;
    private String lastDistance;

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }

    public String getFuel() { return fuel; }
    public void setFuel(String fuel) { this.fuel = fuel; }

    public List<ServiceRecord> getServiceHistory() { return serviceHistory; }
    public void setServiceHistory(List<ServiceRecord> serviceHistory) { this.serviceHistory = serviceHistory; }

    public String getInsuranceExpiry() { return insuranceExpiry; }
    public void setInsuranceExpiry(String insuranceExpiry) { this.insuranceExpiry = insuranceExpiry; }

    public String getNextOilChange() { return nextOilChange; }
    public void setNextOilChange(String nextOilChange) { this.nextOilChange = nextOilChange; }

    public String getNextOilChangeMileage() { return nextOilChangeMileage; }
    public void setNextOilChangeMileage(String nextOilChangeMileage) { this.nextOilChangeMileage = nextOilChangeMileage; }

    public String getAverageConsumption() { return averageConsumption; }
    public void setAverageConsumption(String averageConsumption) { this.averageConsumption = averageConsumption; }

    public String getTechnicalInspectionExpiry() { return technicalInspectionExpiry; }
    public void setTechnicalInspectionExpiry(String technicalInspectionExpiry) { this.technicalInspectionExpiry = technicalInspectionExpiry; }

    public String getLastFuelAmount() { return lastFuelAmount; }
    public void setLastFuelAmount(String lastFuelAmount) { this.lastFuelAmount = lastFuelAmount; }

    public String getLastDistance() { return lastDistance; }
    public void setLastDistance(String lastDistance) { this.lastDistance = lastDistance; }
}