package com.example.pet.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pet.R;
import com.example.pet.utils.ContextStorage;
import com.example.pet.utils.StringResource;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Networking extends Thread {

    private String IP = StringResource.getStringResource(ContextStorage.getCtx(), R.string.AI_Server_IP);
    private int portNum = Integer.parseInt(StringResource.getStringResource(ContextStorage.getCtx(), R.string.AI_Server_Port));
    private byte[] msg;
    public byte[] receive_data;
    public String receive_msg;

    public Networking(@NonNull String name, byte[] msg) {
        super(name);
        this.msg = msg;
    }

    public void setting_msg(byte[] msg) {
        this.msg = msg;
    }

    public void run() {
        try (Socket client = new Socket()) {
            InetSocketAddress ipep = new InetSocketAddress(IP, portNum);

            client.connect(ipep);

            try(OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {

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
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
