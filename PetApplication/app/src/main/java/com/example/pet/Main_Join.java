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
import com.google.firebase.firestore.FirebaseFirestore;

public class Main_Join extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = null;
    private FirebaseFirestore firebaseFirestore = null;

    private EditText etIdJ;
    private EditText etPasswordJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_join);

        //firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // 아이디 & 비밀번호
        etIdJ = (EditText) findViewById(R.id.et_id_join);
        etPasswordJ = (EditText) findViewById(R.id.et_password_join);

        // 회원가입
        Button btnJoin = (Button) findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(view -> {
            String emailStr = etIdJ.getText().toString().trim();
            String passwordStr = etPasswordJ.getText().toString().trim();

            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                // 실패
                Log.w("JOIN", "createUserWithEmail:failure1");
                Toast.makeText(Main_Join.this, "가입 오류1", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // 성공
                                Log.d("JOIN", "createUserWithEmail:success");

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    // User is signed in
                                    // DB 에 User 저장
                                    String userEmail = user.getEmail();
                                    String userUid = user.getUid();

                                    DB_User db_user = new DB_User(userEmail);
                                    firebaseFirestore.collection("Users").document(userUid).set(db_user)
                                            .addOnFailureListener(e -> Log.w("DB_User", "error", e));

                                    // Main_Logo 화면으로 넘어가기
                                    Intent intentMain = new Intent(Main_Join.this, Main_Logo.class);
                                    startActivity(intentMain);
                                } else {
                                    // No user is signed in
                                    // 실패
                                    Log.w("JOIN", "createUserWithEmail:failure2");
                                    Toast.makeText(Main_Join.this, "가입 오류2", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // 실패
                                Log.w("JOIN", "createUserWithEmail:failure3", task.getException());
                                Toast.makeText(Main_Join.this, "가입 오류3", Toast.LENGTH_SHORT).show();
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
