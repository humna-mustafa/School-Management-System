package com.schoolmanagementsystem.models;

// One admin user row as stored in users.txt, including login and security question fields.
public class User {
    private String userId;
    private String fullName;
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private String role;
    private String status;

    public User(
            String userId,
            String fullName,
            String username,
            String password,
            String securityQuestion,
            String securityAnswer,
            String role,
            String status) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.role = role;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toLine() {
        return String.join(
                "|", userId, fullName, username, password, securityQuestion, securityAnswer, role, status);
    }

    public static User fromLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 8) {
            return null;
        }
        return new User(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
    }
}
