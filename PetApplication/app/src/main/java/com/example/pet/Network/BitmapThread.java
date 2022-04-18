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

    public BitmapThread(@NonNull FFmpegMediaMetadataRetriever mediaMetadataRetriever) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public void run() {

        bitmapQueue = new LinkedList<>();

        while (true) {
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(); // current frame

            bitmapQueue.offer(bitmap);

            Log.d("BITMAPBITMAPBITMAP", String.valueOf(bitmap));

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
