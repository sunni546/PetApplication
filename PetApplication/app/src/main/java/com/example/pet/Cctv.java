package com.example.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pet.Network.Networking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class Cctv extends AppCompatActivity {

    private static String cctvUrlStr;
    private String petNameStr;

    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    private FFmpegMediaMetadataRetriever mediaMetadataRetriever;

    byte [] msg = null;

    public static String format_yyyyMMdd_HHmm = "yyyy-MM-dd hh:mm";
    private TextView tvCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cctv);

        // 현재 시간 출력하기
        tvCurrentTime = findViewById(R.id.tv_current_time);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd_HHmm, Locale.getDefault());
        String currentTimeStr = format.format(currentTime);
        tvCurrentTime.setText(currentTimeStr);

        // Info 화면에서 pet 이름, cctv_url 받아오기
        Intent intentInfo = new Intent(this.getIntent());
        petNameStr = intentInfo.getStringExtra("petName");
        cctvUrlStr = intentInfo.getStringExtra("cctvUrl");

        libVlc = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = findViewById(R.id.videoLayout);
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

        /*
        while (<condition to break loop>) {
            FFmpegMediaMetadataRetriever mediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(<stream URL>);
            Bitmap b = mediaMetadataRetriever.getFrameAtTime(); // current frame
            mediaMetadataRetriever.release();
            //Pause for 5 seconds
            Thread.sleep(5000);
        */

        mediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(cctvUrlStr);
        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds

        /*
        Networking networking = new Networking(petNameStr, msg);

        msg = bitmapToByteArray(bitmap);

        networking.setting_msg(msg);

        networking.run();
        */

        mediaMetadataRetriever.release();
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

    // Bitmap을 Byte로 변환
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}

