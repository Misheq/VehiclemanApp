package com.example.mihael.vehiclemanapp.helpers;

/**
 * Simple collection of frequently used constants
 */

public class Constants {

    // api constants
    public static final String MANAGERS = "managers";
    public static final String PERSONS = "persons";
    public static final String VEHICLES = "vehicles";
    public static final String SEPARATOR = "/";
    public static final String ID = "id";
    public static final String ID_PARAM = "{id}";

    // server constants
    public static final String DOMAIN = "192.168.0.104";
    public static final String PORT = ":3334";
    public static final String BASE_URL = "http://" + DOMAIN + PORT + "/vehicleman/api/";

    // spinner constants
    public static final String SELECT_VEHICLE = "Select vehicle";
    public static final String SELECT_PERSON = "Select person";
    public static final String SELECT = "Select";
    public static final String PERSON = "person";
    public static final String VEHICLE = "vehicle";

    // messages

    // required fields
    public static final String FIRST_NAME_REQUIRED = "Please enter first name.";
    public static final String LAST_NAME_REQUIRED = "Please enter last name.";
    public static final String EMAIL_REQUIRED = "Please enter email.";
    public static final String VEHICLE_TYPE_REQUIRED = "Please enter vehicle type.";
    public static final String REG_NUM_REQUIRED = "Please enter registration number.";
}
