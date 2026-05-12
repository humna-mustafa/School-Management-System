package com.schoolmanagementsystem.utils;

import com.schoolmanagementsystem.models.User;

// Remembers which admin is logged in until they log out or close the app.
public final class SessionManager {
    private static User currentUser;

    private SessionManager() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
