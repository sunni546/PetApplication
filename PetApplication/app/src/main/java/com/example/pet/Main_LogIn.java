package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main_LogIn extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = null;

    private EditText etId;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_log_in);

        //firebaseAuth 의 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 아이디 & 비밀번호
        etId = (EditText) findViewById(R.id.et_id);
        etPassword = (EditText) findViewById(R.id.et_password);

        // 로그인
        Button btnLogIn = (Button) findViewById(R.id.btn_log_in);
        btnLogIn.setOnClickListener(view -> {
            String emailStr = etId.getText().toString().trim();
            String passwordStr = etPassword.getText().toString().trim();

            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                // 실패
                Log.w("LOG_IN", "signInWithEmail:failure1");
                Toast.makeText(this, "로그인 오류1", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // 성공
                                Log.d("LOG_IN", "signInWithEmail:success");

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    // User is signed in
                                    // Home 화면으로 넘어가기
                                    Intent intentHome = new Intent(Main_LogIn.this, Home.class);
                                    startActivity(intentHome);
                                } else {
                                    // No user is signed in
                                    // 실패
                                    Log.w("LOG_IN", "signInWithEmail:failure2");
                                    Toast.makeText(this, "로그인 오류2", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // 실패
                                Log.w("LOG_IN", "signInWithEmail:failure3", task.getException());
                                Toast.makeText(Main_LogIn.this, "로그인 오류3", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // 활동 초기화 시 사용자가 로그인되어 있는지 확인
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
}