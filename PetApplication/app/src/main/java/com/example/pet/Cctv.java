package com.example.pet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pet.Network.BitmapThread;
import com.example.pet.Network.Networking;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.text.SimpleDateFormat;
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

        // BitmapThread
        mediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(cctvUrlStr);
        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);

        BitmapThread bitmapThread = new BitmapThread(mediaMetadataRetriever);
        bitmapThread.start();

        Networking networking = new Networking(petNameStr);
        networking.start();

        // mediaMetadataRetriever.release();
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

