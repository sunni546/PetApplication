package com.example.pet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Home_ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Home_ListViewItem> listViewItemList = new ArrayList<Home_ListViewItem>() ;

    // 생성자
    Home_ListViewAdapter() { }

    // Adapter에 사용되는 데이터의 개수 리턴.
    // 필수
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View 리턴.
    // 필수
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final ImageView photoImageView = (ImageView) convertView.findViewById(R.id.imageView_petPhoto) ;
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.textView_petName) ;
        final TextView detailTextView = (TextView) convertView.findViewById(R.id.textView_petDetail) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Home_ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        photoImageView.setImageDrawable(listViewItem.getPhoto());
        nameTextView.setText(listViewItem.getName());
        detailTextView.setText(listViewItem.getDetail());

        // button1 클릭 시 TextView(textView1)의 내용 변경.
        ImageButton btn_delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 아이템 삭제
                listViewItemList.remove(pos) ;

                // listview 갱신.
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID 리턴.
    // 필수
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴.
    // 필수
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가.
    public void addItem(Drawable photo, String name, String detail) {
        Home_ListViewItem item = new Home_ListViewItem();

        item.setPhoto(photo);
        item.setName(name);
        item.setDetail(detail);

        listViewItemList.add(item);
    }
}
