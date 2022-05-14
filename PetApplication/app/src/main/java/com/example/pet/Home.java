package com.example.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Home extends AppCompatActivity {

    public static Context hContext;

    private Home_ListViewAdapter adapter;
    private ListView listviewPet;
    private View viewHome;

    private Boolean firstPet = true;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        hContext = this;

        // 참조
        listviewPet = findViewById(R.id.listview_pet);
        viewHome = findViewById(R.id.v_home);

        //firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userUid = user.getUid();

        // pet 유무 확인
        DocumentReference docRefUsers = firebaseFirestore.collection("Users").document(userUid);
        docRefUsers.get().addOnSuccessListener(documentSnapshot -> {
            DB_User userDB = documentSnapshot.toObject(DB_User.class);
            assert userDB != null;

            firstPet = userDB.getNumPets() == 0;

            // pet 리스트 생성
            if (firstPet) {
                isFirstPet();
            } else {
                makeListviewPet();
            }
        });

        // pet 추가 버튼
        ImageButton btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(view -> {
            // pet 정보 추가 화면으로 넘어가기
            Intent intentProfile_add = new Intent(getApplicationContext(),Profile_add.class);
            startActivity(intentProfile_add);
        });

        // listviewPet 에 클릭 이벤트 핸들러 정의.
        Intent intentInfo = new Intent(this, Info.class);
        listviewPet.setOnItemClickListener((parent, v, position, id) -> {
            // get item
            Home_ListView_Item item = (Home_ListView_Item) parent.getItemAtPosition(position);

            String petNameStr = item.getName();

            // pet 정보 화면으로 넘어가기
            intentInfo.putExtra("petName", petNameStr);
            startActivity(intentInfo);
        });

        // 로그아웃 버튼
        ImageButton btnLogOut = findViewById(R.id.btn_log_out);
        btnLogOut.setOnClickListener(view -> {
            // 로그아웃
            FirebaseAuth.getInstance().signOut();
            // Main_Logo 화면으로 넘어가기
            Intent intentMainLogo = new Intent(getApplicationContext(), Main_Logo.class);
            startActivity(intentMainLogo);
            finish();
        });
    }

    public void makeListviewPet() {
        isNotFirstPet();

        // Adapter 생성
        adapter = new Home_ListViewAdapter();

        // Adapter 달기
        listviewPet.setAdapter(adapter);

        //firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userUid = user.getUid();

        firebaseFirestore.collection("Users").document(userUid).collection("Pets")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("LISTVIEWPET", document.getId() + " => " + document.getData());

                            // Cat/Dog 프사
                            String CatDogStr = document.get("cat_dog").toString();
                            int CatDog = R.drawable.ic_launcher_foreground;
                            switch (CatDogStr){
                                case "고양이":
                                    CatDog = R.drawable.img_9;
                                    break;
                                case "강아지":
                                    CatDog = R.drawable.img_3;
                                    break;
                            }

                            adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), CatDog),
                                    document.get("name").toString(), document.get("age").toString() + "세");

                            // listview 갱신
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("LISTVIEWPET", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void isFirstPet() {
        viewHome.setVisibility(View.GONE);
        listviewPet.setVisibility(View.GONE);
    }
    public void isNotFirstPet() {
        viewHome.setVisibility(View.VISIBLE);
        listviewPet.setVisibility(View.VISIBLE);
    }
}
