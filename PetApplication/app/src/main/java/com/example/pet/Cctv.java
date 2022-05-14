package com.example.pet;

import static com.example.pet.Chart_AbnormalBehavior.format_yyMMdd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cctv extends AppCompatActivity {

    private static String cctvUrlStr;

    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    public static String format_yyyyMMdd_HHmm = "yyyy-MM-dd HH:mm";

    TextView tvNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cctv);

        // 현재 시간 출력하기
        TextView tvCurrentTime = findViewById(R.id.tv_current_time);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd_HHmm, Locale.getDefault());
        String currentTimeStr = format.format(currentTime);
        tvCurrentTime.setText(currentTimeStr);

        // Info 화면에서 pet 이름, cctv_url 받아오기
        Intent intentInfo = new Intent(this.getIntent());
        cctvUrlStr = intentInfo.getStringExtra("cctvUrl");
        String petNameStr = intentInfo.getStringExtra("petName");

        libVlc = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = findViewById(R.id.videoLayout);

        // 뒤로가기 버튼
        ImageButton btnCctvBack = findViewById(R.id.btn_cctv_back);
        btnCctvBack.setOnClickListener(view -> {
            // pet 정보 화면으로 넘어가기
            finish();
        });

        // 긴급 알림
        TextView tvToday = findViewById(R.id.tv_today);
        String monthStr = new SimpleDateFormat("M", Locale.getDefault()).format(currentTime);
        String dayStr = new SimpleDateFormat("d", Locale.getDefault()).format(currentTime);
        tvToday.setText(monthStr + "월 " + dayStr + "일");

        tvNotification = findViewById(R.id.notification);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userUid = user.getUid();

        firebaseFirestore.collection("Users")
                .document(userUid).collection("Pets")
                .document(petNameStr).collection("AbnormalBehaviors")
                .orderBy("time", Query.Direction.DESCENDING).limit(1)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("NNNNNNNNNNNNNNNNNNNNN", document.getId() + " => " + document.getData());
                            String documentNameStr = document.getId().substring(0, 6);
                            if (documentNameStr.equals(new SimpleDateFormat(format_yyMMdd, Locale.getDefault()).format(currentTime))) {
                                tvNotification.setText(String.valueOf(document.get("time")).substring(7) + " 이상 행동 발생");
                            }
                            else {
                                tvNotification.setText("이상 행동 없음");
                            }
                        }
                    } else {
                        Log.d("NNNNNNNNNNNNNNNNNNNNN", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mediaPlayer.attachViews(videoLayout, null, false, false);

        Media media = new Media(libVlc, Uri.parse(cctvUrlStr));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=600");

        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.release();
        libVlc.release();
    }
}

