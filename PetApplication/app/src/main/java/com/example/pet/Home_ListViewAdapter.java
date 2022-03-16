package com.example.pet;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Home_ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Home_ListView_Item> listViewItemList = new ArrayList<Home_ListView_Item>() ;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userUid = user.getUid();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo) ;
        final TextView tvName = (TextView) convertView.findViewById(R.id.tv_name) ;
        final TextView tvDetail = (TextView) convertView.findViewById(R.id.tv_detail) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Home_ListView_Item listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ivPhoto.setImageDrawable(listViewItem.getPhoto());
        tvName.setText(listViewItem.getName());
        tvDetail.setText(listViewItem.getDetail());


        String Petname=listViewItem.getName();
        // button1 클릭 시 TextView(textView1)의 내용 변경.
        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 아이템 삭제
                listViewItemList.remove(pos);

                // DB 삭제
                DocumentReference docRefUsers2 = firebaseFirestore.collection("Users").
                        document(userUid).collection("Pets").document(Petname).
                        collection("AbnormalBehaviors").document("AbnormalBehaviors");
                docRefUsers2.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                DocumentReference docRefUsers3 = firebaseFirestore.collection("Users").
                        document(userUid).collection("Pets").document(Petname).
                        collection("Actions").document("Actions");
                docRefUsers3.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                DocumentReference docRefUsers4 = firebaseFirestore.collection("Users").
                        document(userUid).collection("Pets").document(Petname).
                        collection("Emotions").document("Emotions");
                docRefUsers4.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                DocumentReference docRefUsers = firebaseFirestore.collection("Users").document(userUid).collection("Pets").document(Petname);
                docRefUsers.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                // DB : 마리 수 감소
                DocumentReference userOfPet = firebaseFirestore.collection("Users").document(userUid);
                userOfPet.update("numPets", FieldValue.increment(-1));

                // listview 갱신.
                ((Home)Home.hContext).makeListviewPet();
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
        Home_ListView_Item item = new Home_ListView_Item();

        item.setPhoto(photo);
        item.setName(name);
        item.setDetail(detail);

        listViewItemList.add(item);
    }
}
