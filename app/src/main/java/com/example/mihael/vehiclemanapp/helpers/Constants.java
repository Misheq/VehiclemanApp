package com.example.mihael.vehiclemanapp.helpers;

/**
 * Simple collection of frequently used constants
 */

public class Constants {

    // api constants
    public static final String MANAGERS = "managers";
    public static final String AUTHORIZATION = "Authorization";
    public static final String PERSONS = "persons";
    public static final String VEHICLES = "vehicles";
    public static final String SEPARATOR = "/";
    public static final String ID = "id";
    public static final String ID_PARAM = "{id}";
    public static final String EMAIL = "email";
    public static final String EMAIL_PARAM = "{email}";
    public static final String AUTH = "auth";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";

    // server constants (local)
    // public static final String DOMAIN = "192.168.0.104";
    // public static final String PORT = ":3334";
    // public static final String LOCAL_URL = "http://" + DOMAIN + PORT + "/vehicleman/api/";
    public static final String SERVICE_URL = "https://vehiclemani333373trial.hanatrial.ondemand.com/vehicleman/api/";

    // spinner constants
    public static final String SELECT_VEHICLE = "Select vehicle";
    public static final String SELECT_PERSON = "Select person";
    public static final String SELECT = "Select";
    public static final String PERSON = "person";
    public static final String VEHICLE = "vehicle";

    // messages
    public static final String TAP_TO_SET_SERVICING_DATE = " Tap to set servicing date";
    public static final String SERVICING_DATE = " Servicing date: ";
    // public static final String LOADING_VEHICLES_FAILED = "Loading vehicles failed.\nPlease try again.";
    // public static final String LOADING_PERSONS_FAILED = "Loading employees failed.\nPlease try again.";
    public static final String MANAGER_REGISTERED = "Manager registered";
    public static final String MANAGER_UPDATED = "Manager updated";
    public static final String UPDATE_VEHICLE_SUCCESSFUL = "Vehicle updated";
    public static final String UPDATE_PERSON_SUCCESSFUL = "Person updated";
    public static final String CREATE_VEHICLE_SUCCESSFUL = "Vehicle created";
    public static final String CREATE_PERSON_SUCCESSFUL = "Employee created";

    public static final String DELETE_VEHICLE_SUCCESSFUL = "Vehicle deleted";
    public static final String DELETE_PERSON_SUCCESSFUL = "Person deleted";
    //public static final String DELETE_FAILED = "Delete failed.\nPlease try again.";
    // public static final String WRONG_CREDENTIALS = "Incorrect email or password.\nPlease try again.";
    // public static final String SERVER_ERROR = "Server error.\nPlease try again later.";
    public static final String CONNECTION_FAILED = "Connection failed.\nPlease check internet connection.";
    public static final String DIALOG_BOX_MESSAGE = "Are you sure you want to delete this element?";


    // required fields
    public static final String FIRST_NAME_REQUIRED = "Please enter first name.";
    public static final String LAST_NAME_REQUIRED = "Please enter last name.";
    public static final String EMAIL_FORMAT_INVALID = "Please check email format.";
    public static final String VEHICLE_TYPE_REQUIRED = "Please enter vehicle type.";
    public static final String REG_NUM_REQUIRED = "Please enter registration number.";
    public static final String PASSWORD_TOO_SHORT = "Please enter password of minimum ";
    public static final String PASSWORD_CHARACTERS = " characters.";
    public static final String PASSWORD_NOT_MATCH = "Your password does not match.";

    // titles and names
    public static final String SIGN_IN = "Sign-in";
    public static final String SUBMIT = "Submit";
    public static final String SAVE = "Save";
    public static final String NEW = "New";
    public static final String DIALOG_BOX_TITLE = "Delete confirmation";
    public static final String DIALOG_BOX_YES = "Yes";
    public static final String DIALOG_BOX_NO = "No";
}
