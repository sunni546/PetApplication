package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Info_PetInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_pet_info);

        // Info 화면에서 pet 이름 받아오기
        Intent intentInfo = new Intent(this.getIntent());
        String petNameStr = intentInfo.getStringExtra("petName");

        // TODO: pet name
        TextView tv = (TextView) findViewById(R.id.tv_info_pet_info);
        tv.setText(petNameStr);
    }
}
