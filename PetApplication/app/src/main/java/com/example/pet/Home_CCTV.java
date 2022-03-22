package com.example.pet;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

public class Home_CCTV extends AppCompatActivity
{
    private static final String url = "rtsp://172.30.1.56:8554/stream.sdp";
    // private static final String url = "http://172.30.1.52:8080/video";

    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_cctv);

        libVlc = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = findViewById(R.id.videoLayout);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mediaPlayer.attachViews(videoLayout, null, false, false);

        Media media = new Media(libVlc, Uri.parse(url));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=600");

        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mediaPlayer.release();
        libVlc.release();
    }
}

