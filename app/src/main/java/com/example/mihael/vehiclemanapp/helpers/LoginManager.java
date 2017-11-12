package com.example.mihael.vehiclemanapp.helpers;

import com.example.mihael.vehiclemanapp.entities.Manager;

public final class LoginManager {

    private static String logedInManagerToken;
    private static Manager manager;

    private LoginManager() {
        logedInManagerToken = "";
    }

    public static String getLogedInManagerToken() {
        return logedInManagerToken;
    }

    public static void setLogedInManagerToken(String logedInManagerToken) {
        LoginManager.logedInManagerToken = logedInManagerToken;
    }

    public static int getManagerId() {
        return LoginManager.manager.getManagerId();
    }

    public static Manager getManager() {
        return manager;
    }

    public static void setManager(Manager manager) {
        LoginManager.manager = manager;
    }
}
