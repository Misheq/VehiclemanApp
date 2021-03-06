package com.example.mihael.vehiclemanapp.entities;

import java.io.Serializable;

/**
 * Vehicle model representation
 * This is a vehicle (car, motorbike, utility...) in a company
 * A vehicle can have an assigned employee
 */

public class Vehicle implements Serializable {

    private int vehicleId;
    private String vehicleType;
    private String registrationNumber;
    private String assigneeId;
    private Person person;
    private int managerId;
    private String color;
    private String description;
    private String servicingDate;

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

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServicingDate() {
        return servicingDate;
    }

    public void setServicingDate(String servicingDate) {
        this.servicingDate = servicingDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return vehicleType + " " + registrationNumber;
    }
}

