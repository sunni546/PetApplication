package com.example.pet;

public class DB_Pet {

    private String petName;

    // 생성자
    public DB_Pet() { }

    public DB_Pet(String petName) {
        this.petName = petName;
    }

    // 정보
    // 이름
    public String getPetName() {
        return petName;
    }
    public void setPetName(String petName) {
        this.petName = petName;
    }
}
