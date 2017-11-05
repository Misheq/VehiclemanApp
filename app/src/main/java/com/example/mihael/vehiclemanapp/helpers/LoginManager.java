package com.example.mihael.vehiclemanapp.helpers;

public final class LoginManager {

    private static String logedInManagerToken;
    private static int managerId;

    private LoginManager() {
        logedInManagerToken = "";
        managerId = -1;
    }

    public static String getLogedInManagerToken() {
        return logedInManagerToken;
    }

    public static void setLogedInManagerToken(String logedInManagerToken) {
        LoginManager.logedInManagerToken = logedInManagerToken;
    }

    public static int getManagerId() {
        return managerId;
    }

    public static void setManagerId(int managerId) {
        LoginManager.managerId = managerId;
    }
}
