package com.example.pet.Network;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class BitmapThread extends Thread {

    public static Queue<Bitmap> bitmapQueue;

    private FFmpegMediaMetadataRetriever mediaMetadataRetriever;



    byte [] msg = null;

    public BitmapThread(@NonNull FFmpegMediaMetadataRetriever mediaMetadataRetriever) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public void run() {

        bitmapQueue = new LinkedList<>();

        while (true) {
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(); // current frame

            bitmapQueue.offer(bitmap);

            /*
            msg = bitmapToByteArray(bitmap);

            Networking networking = new Networking(petNameStr, msg);

            networking.setting_msg(msg);

            networking.run();

            try {
                networking.join();
                // Pause for 5 seconds
                // networking.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

            Log.d("BITMAPBITMAPBITMAP", String.valueOf(bitmap));

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
