package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Join extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        // 회원가입
        Button btnJoin = (Button) findViewById(R.id.btn_join);
        Intent intentMain = new Intent(this, Main.class);
        btnJoin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pet 추가 화면으로 넘어가기
                startActivity(intentMain);
            }
        });
    }
}
