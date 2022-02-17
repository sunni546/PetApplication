package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {

    boolean firstPet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view -> {
            Intent intent_cctv = new Intent(getApplicationContext(),Profile_add.class);
            startActivity(intent_cctv);
        }));

        // Adapter 생성
        Home_ListViewAdapter adapter = new Home_ListViewAdapter();

        // ListView 참조 및 Adapter 달기
        ListView listviewPet = (ListView) findViewById(R.id.listview_pet);
        listviewPet.setAdapter(adapter);

        // 처음 pet 추가시
        View viewEmpty = (View) findViewById(R.id.view1);
        isFirstPet(viewEmpty, listviewPet);


        // 1
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground),
                "멍구", "견생 2년차");
        // 2
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground),
                "돌비", "묘생 1년차");
        // 3
        // adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground),
        //         "체리", "견생 3년차");
        // 4
        // adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground),
        //         "해피", "견생 1년차");
        // 5
        // adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground),
        //         "나비", "묘생 4년차");
        

        // pet 추가 버튼
        ImageButton btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(view -> {
            // pet 정보 추가 화면으로 넘어가기
            Intent intentProfile_add = new Intent(getApplicationContext(),Profile_add.class);
            startActivity(intentProfile_add);
            // listview 갱신
            adapter.notifyDataSetChanged();
        });


        Intent intentInfo = new Intent(this, Info.class);
        // 위에서 생성한 listview 에 클릭 이벤트 핸들러 정의.
        listviewPet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Home_ListView_Item item = (Home_ListView_Item) parent.getItemAtPosition(position);

                String petNameStr = item.getName();

                // pet 정보 화면으로 넘어가기
                intentInfo.putExtra("petName", petNameStr);
                startActivity(intentInfo);

                // 클릭 확인하기
                // Toast.makeText(getApplicationContext(), petNameStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void isFirstPet(View v, ListView lv){
        if (firstPet) {
            v.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
        }
        else{
            v.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
        }
    }
}
