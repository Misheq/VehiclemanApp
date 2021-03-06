package com.example.mihael.vehiclemanapp.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Person model representation
 * This is an employee in a company
 * An employee can have an assigned vehicle
 */

public class Person implements Serializable {

    private int personId;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName = "";
    private String phone = "";
    private List<Vehicle> vehicles;
    private int managerId;

    public Person() {
        vehicles = new ArrayList<>();
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

