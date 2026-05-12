package com.schoolmanagementsystem.services;

import com.schoolmanagementsystem.models.User;
import com.schoolmanagementsystem.storage.FileManager;
import com.schoolmanagementsystem.utils.Constants;
import com.schoolmanagementsystem.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// Handles signup, login, password reset, and saving the users file for admin accounts.
public class AuthService {
    private final FileManager fileManager;

    public AuthService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (String line : fileManager.readAll(Constants.USERS_FILE)) {
            if (!line.isBlank()) {
                User user = User.fromLine(line);
                if (user != null) users.add(user);
            }
        }
        return users;
    }

    public boolean usernameExists(String username) {
        return getAllUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    public User login(String username, String password) {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean signup(String fullName, String username, String password, String question, String answer) {
        if (usernameExists(username)) return false;
        List<User> users = getAllUsers();
        String nextId = IdGenerator.nextId("ADM", users.stream().map(User::getUserId).toList());
        users.add(new User(nextId, fullName, username, password, question, answer, "ADMIN", "ACTIVE"));
        saveUsers(users);
        return true;
    }

    public boolean resetPassword(String username, String answer, String newPassword) {
        List<User> users = getAllUsers();
        boolean updated = false;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getSecurityAnswer().equalsIgnoreCase(answer)) {
                u.setPassword(newPassword);
                updated = true;
                break;
            }
        }
        if (updated) saveUsers(users);
        return updated;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.isBlank()) return false;
        if (!newPassword.equals(confirmPassword)) return false;
        List<User> users = getAllUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(oldPassword)) {
                u.setPassword(newPassword);
                saveUsers(users);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAccount(String username) {
        List<User> users = getAllUsers();
        long activeAdmins = users.stream()
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()) && "ACTIVE".equalsIgnoreCase(u.getStatus()))
                .count();
        User target = users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
        if (target == null) {
            return false;
        }
        if (activeAdmins <= 1
                && "ACTIVE".equalsIgnoreCase(target.getStatus())
                && "ADMIN".equalsIgnoreCase(target.getRole())) {
            return false;
        }
        users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
        saveUsers(users);
        return true;
    }

    public void saveUsers(List<User> users) {
        fileManager.writeAll(Constants.USERS_FILE, users.stream().map(User::toLine).toList());
    }
}
