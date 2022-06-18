package util;

import android.app.Application;

// Extend Application to make it accessible throughout the whole Application
// This class is responsible for holding the details of the current user
public class ShopSaverApi extends Application {

    private String username;
    private String userId;
    private String userEmail;

    // Create a singleton to be referred to
    private static ShopSaverApi instance;

    // We return the same instance
    // Only if it is null, we create a new one
    public static ShopSaverApi getInstance() {
        if (instance == null) {
            instance = new ShopSaverApi();
        }
        return instance;
    }

    // Constructor
    public ShopSaverApi(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
