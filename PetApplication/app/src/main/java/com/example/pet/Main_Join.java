package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main_Join extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private Button btnJoin;
    private EditText etIdJ;
    private EditText etPasswordJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_join);

        //firebaseAuth 의 인스턴스
        firebaseAuth = firebaseAuth.getInstance();

        // 아이디 & 비밀번호
        etIdJ = (EditText) findViewById(R.id.et_id_join);
        etPasswordJ = (EditText) findViewById(R.id.et_password_join);

        // 회원가입
        btnJoin = (Button) findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(view -> {
            String emailStr = etIdJ.getText().toString().trim();
            String passwordStr = etPasswordJ.getText().toString().trim();

            firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(Main_Join.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 성공
                                // Main_Logo 화면으로 넘어가기
                                Intent intentMain = new Intent(Main_Join.this, Main_Logo.class);
                                startActivity(intentMain);
                            } else {
                                // 실패
                                Toast.makeText(Main_Join.this, "가입 오류", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}
