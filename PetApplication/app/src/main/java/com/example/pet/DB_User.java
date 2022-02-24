package com.example.pet;

public class DB_User {

    private String userEmail;

    // 생성자
    public DB_User() { }

    public DB_User(String userEmail) {
        this.userEmail = userEmail;
    }

    // 이메일
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
