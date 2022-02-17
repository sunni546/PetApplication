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

        ImageButton btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pet 추가 페이지로 넘어가기
                Intent intent_sleep = new Intent(getApplicationContext(),Profile_add.class);
                startActivity(intent_sleep);

                // listview 갱신
                adapter.notifyDataSetChanged();
            }
        });


        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listviewPet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Home_ListViewItem item = (Home_ListViewItem) parent.getItemAtPosition(position);

                Drawable photoDrawable = item.getPhoto();
                String nameStr = item.getName();
                String detailStr = item.getDetail();

                // TODO : use item data.
                Toast.makeText(getApplicationContext(), nameStr + "는 " + detailStr, Toast.LENGTH_SHORT).show();
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