package com.example.pet.Network;

import static com.example.pet.Network.BitmapThread.bitmapQueue;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pet.R;
import com.example.pet.utils.ContextStorage;
import com.example.pet.utils.StringResource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Networking extends Thread {

    private String IP;
    private int portNum;
    private byte[] msg;
    public byte[] receive_data;
    public String receive_msg;

    public Networking(@NonNull String name) {
        super(name);
    }

    public void setting_msg(Bitmap msg) {
        this.msg = bitmapToByteArray(msg);
    }

    public void run() {
        while (true) {
            if (bitmapQueue.size() > 0) {
                try (Socket client = new Socket()) {
                    InetSocketAddress ipep = new InetSocketAddress(IP, portNum);

                    client.connect(ipep);

                    Bitmap first_image = bitmapQueue.poll();
                    setting_msg(first_image);

                    try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {

                        byte[] size = ByteBuffer.allocate(4).putInt(this.msg.length).array();

                        sender.write(size);
                        sender.write(this.msg);
                        sender.flush();

                        receive_data = new byte[4];

                        receiver.read(receive_data, 0, 4);

                        ByteBuffer c = ByteBuffer.wrap(receive_data);
                        c.order(ByteOrder.LITTLE_ENDIAN);

                        int length = c.getInt();

                        receive_data = new byte[length];

                        receiver.read(receive_data, 0, length);

                        receive_msg = new String(receive_data, "UTF-8");
                        System.out.println(receive_msg);

                        client.close();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Bitmap을 Byte로 변환
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
