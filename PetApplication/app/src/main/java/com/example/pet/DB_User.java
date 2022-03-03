package com.example.pet;

public class DB_User {

    private String userEmail;
    private Integer numPets;

    // 생성자
    public DB_User() {
        this.userEmail = "";
        this.numPets = 0;
    }

    public DB_User(String userEmail) {
        this.userEmail = userEmail;
        this.numPets = 0;
    }

    // 이메일
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // pet 수
    public Integer getNumPets() {
        return numPets;
    }

    public void setNumPets(Integer numPets) {
        this.numPets = numPets;
    }
}
