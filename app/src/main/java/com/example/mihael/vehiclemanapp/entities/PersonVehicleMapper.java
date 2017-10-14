package com.example.mihael.vehiclemanapp.entities;

import java.util.ArrayList;
import java.util.List;

public class PersonVehicleMapper {

    Person person;
    List<Vehicle> vehicles;

    public PersonVehicleMapper() {
        vehicles = new ArrayList<>();
    }

    public PersonVehicleMapper(Person person, List<Vehicle> vehicles) {
        this.person = person;
        this.vehicles = vehicles;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Vehicle> vehicles() {
        return vehicles;
    }

    public void setVehicleList(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
