package com.example.mihael.vehiclemanapp.helpers;

public final class LoginManager {

    private static String logedInManagerToken;

    private LoginManager() {
        logedInManagerToken = "";
    }

    public static String getLogedInManagerToken() {
        return logedInManagerToken;
    }

    public static void setLogedInManagerToken(String logedInManagerToken) {
        LoginManager.logedInManagerToken = logedInManagerToken;
    }
}
