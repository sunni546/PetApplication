package com.example.pet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // Home 화면에서 pet 이름 받아오기
        Intent intentHome = new Intent(this.getIntent());
        String nameStr = intentHome.getStringExtra("name");
    }
}
