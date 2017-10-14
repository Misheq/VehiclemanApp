package com.example.mihael.vehiclemanapp.entities;

public class Vehicle {

    private int vehicleId;
    private String vehicleType;
    private String registrationNumber;

    public Vehicle() {

    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleType='" + vehicleType + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    // String brand;
    // String model;
    // String company;
    // Date dateOfLastCheck;
    // Date dateOfNextCheck;
    // Date dateOfAquirement;
    // Date registrationDate;
    // Date registrationExpirationDate;
    // int totalDistance;
    // int height;
    // int width;
    // int length;
    // int weight;
    // List<BufferedImage> pictures;
}

