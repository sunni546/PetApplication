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

public class Home extends AppCompatActivity {

    boolean firstPet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

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

        // pet 추가 버튼
        ImageButton btn_add = (ImageButton) findViewById(R.id.btn_add);
        Intent intentAdd = new Intent(this, Add.class);
        btn_add.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pet 추가 화면으로 넘어가기
                startActivity(intentAdd);
                // listview 갱신
                adapter.notifyDataSetChanged();
            }
        });

        Intent intentInfo = new Intent(this, Info.class);
        // 위에서 생성한 listview 에 클릭 이벤트 핸들러 정의.
        listviewPet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Home_ListViewItem item = (Home_ListViewItem) parent.getItemAtPosition(position);

                String nameStr = item.getName();

                // pet 정보 화면으로 넘어가기
                intentInfo.putExtra("name", nameStr);
                startActivity(intentInfo);

                // 클릭 확인하기
                // Toast.makeText(getApplicationContext(), nameStr, Toast.LENGTH_SHORT).show();
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