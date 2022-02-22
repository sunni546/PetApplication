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

public class Main_LogIn extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private Button btnLogIn;
    private EditText etId;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_log_in);

        //firebaseAuth 의 인스턴스
        firebaseAuth = firebaseAuth.getInstance();

        // 아이디 & 비밀번호
        etId = (EditText) findViewById(R.id.et_id);
        etPassword = (EditText) findViewById(R.id.et_password);

        // 로그인
        btnLogIn = (Button) findViewById(R.id.btn_log_in);
        btnLogIn.setOnClickListener(view -> {
            String emailStr = etId.getText().toString().trim();
            String passwordStr = etPassword.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(Main_LogIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 성공
                                // Home 화면으로 넘어가기
                                Intent intentHome = new Intent(Main_LogIn.this, Home.class);
                                startActivity(intentHome);
                            } else {
                                // 실패
                                Toast.makeText(Main_LogIn.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}