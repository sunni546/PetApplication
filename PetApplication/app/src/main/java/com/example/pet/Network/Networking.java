package com.example.pet.Network;

import static android.content.ContentValues.TAG;
import static com.example.pet.Info.format_yyMMdd_HH;
import static com.example.pet.Network.BitmapThread.bitmapQueue;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Networking extends Thread {

    private String name;
    private String IP = "";
    private int portNum;
    private byte[] msg;
    public byte[] receive_data;
    public String receive_msg;

    public static String format_yyMMdd_HHmm2 = "yyMMdd_HH:mm";

    public Networking(@NonNull String name) {
        this.name = name;
    }

    public void setting_msg(Bitmap msg) {
        this.msg = bitmapToByteArray(msg);
    }

    public void run() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userUid = user.getUid();

        while (true) {
            //시간
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat(format_yyMMdd_HH, Locale.getDefault());
            SimpleDateFormat format2 = new SimpleDateFormat(format_yyMMdd_HHmm2, Locale.getDefault());
            String currentTimeStr = format.format(currentTime);
            String currentTimeStr2 = format2.format(currentTime);

            if (bitmapQueue.size() > 0) {
                try (Socket client = new Socket()) {
                    InetSocketAddress ipep = new InetSocketAddress(IP, portNum);

                    client.connect(ipep);

                    Bitmap first_image = bitmapQueue.poll();

                    bitmapQueue.poll();

                    setting_msg(first_image);

                    try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream()) {
                        String length_ = Integer.toString(this.msg.length);

                        byte[] data = length_.getBytes();

                        ByteBuffer b = ByteBuffer.allocate(4);

                        b.order(ByteOrder.LITTLE_ENDIAN);
                        b.putInt(data.length);

                        sender.write(b.array(), 0, 4);
                        sender.write(data);

                        sender.write(this.msg);
                        sender.flush();

                        receive_data = new byte[64];

                        receiver.read(receive_data, 0, 64);

                        ByteBuffer c = ByteBuffer.wrap(receive_data);
                        c.order(ByteOrder.LITTLE_ENDIAN);

                        int length = c.getInt();

                        receive_data = new byte[length];

                        receiver.read(receive_data, 0, length);

                        receive_msg = new String(receive_data, "UTF-8");

                        System.out.println(receive_msg);
                        Log.d("NetworkingNetworking", receive_msg);

                        if (receive_msg.length() > 1) {
                            String[] splitText=receive_msg.split(" ");
                            String Emo = splitText[3]; // 감정
                            String Act = splitText[4]; // 행동

                            //Emotion DB 저장
                            DocumentReference userOfPet = firebaseFirestore.collection("Users")
                                    .document(userUid).collection("Pets")
                                    .document(name).collection("Emotions")
                                    .document(currentTimeStr);
                            userOfPet.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        switch (Emo){
                                            case "Happy" :
                                                userOfPet.update("행복", FieldValue.increment(1));
                                                break;
                                            case "Fear" :
                                                userOfPet.update("공포", FieldValue.increment(1));
                                                break;
                                            case "Unrest/Sad" :
                                                userOfPet.update("불안", FieldValue.increment(1));
                                                break;
                                            case "Relax" :
                                                userOfPet.update("평안", FieldValue.increment(1));
                                                break;
                                            case "Angry/Discomfort" :
                                                userOfPet.update("화남", FieldValue.increment(1));
                                                break;
                                            case "Aggression" :
                                                userOfPet.update("공격성", FieldValue.increment(1));
                                                break;
                                            default :
                                                break;
                                        }
                                        Log.d(TAG, "Document exists!");
                                    }
                                    else {
                                        Map<String, Object> Emotion = new HashMap<>();
                                        Emotion.put("행복", 0);
                                        Emotion.put("평안", 0);
                                        Emotion.put("불안", 0);
                                        Emotion.put("화남", 0);
                                        Emotion.put("공포", 0);
                                        Emotion.put("공격성", 0);
                                        userOfPet.set(Emotion);
                                        switch (Emo){
                                            case "Happy" :
                                                userOfPet.update("행복", FieldValue.increment(1));
                                                break;
                                            case "Fear" :
                                                userOfPet.update("공포", FieldValue.increment(1));
                                                break;
                                            case "Unrest/Sad" :
                                                userOfPet.update("불안", FieldValue.increment(1));
                                                break;
                                            case "Relax" :
                                                userOfPet.update("평안", FieldValue.increment(1));
                                                break;
                                            case "Angry/Discomfort" :
                                                userOfPet.update("화남", FieldValue.increment(1));
                                                break;
                                            case "Aggression" :
                                                userOfPet.update("공격성", FieldValue.increment(1));
                                                break;
                                            default :
                                                break;
                                        }
                                        Log.d(TAG, "Document does not exist!");
                                    }
                                }
                                else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }
                            });
                            //--------------------------------------------------------------------

                            //이상행동인 경우 DB 저장
                            if (Act.equals("NOTHING")){
                                DocumentReference userOfPet3 = firebaseFirestore.collection("Users")
                                        .document(userUid).collection("Pets")
                                        .document(name).collection("AbnormalBehaviors")
                                        .document(currentTimeStr);
                                userOfPet3.get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            userOfPet3.update("count", FieldValue.increment(1));
                                            userOfPet3.update("time", currentTimeStr2);
                                            Log.d(TAG, "Document exists!");
                                        } else {
                                            Map<String, Object> AB_ACT = new HashMap<>();
                                            AB_ACT.put("count", 0);
                                            AB_ACT.put("time", 0);
                                            userOfPet3.set(AB_ACT);
                                            userOfPet3.update("count", FieldValue.increment(1));
                                            userOfPet3.update("time", currentTimeStr2);
                                            Log.d(TAG, "Document does not exist!");
                                        }
                                    } else {
                                        Log.d(TAG, "Failed with: ", task.getException());
                                    }
                                });
                            }

                            //이상행동이 아닌 경우 DB 저장
                            else{
                                DocumentReference userOfPet2 = firebaseFirestore.collection("Users")
                                        .document(userUid).collection("Pets")
                                        .document(name).collection("Actions")
                                        .document(currentTimeStr);
                                userOfPet2.get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            switch (Act){
                                                case "LYING" :
                                                case "SITDOWN" :
                                                    userOfPet2.update("휴식 시간", FieldValue.increment(1));
                                                    break;
                                                default :
                                                    userOfPet2.update("운동 시간", FieldValue.increment(1));
                                                    break;
                                            }
                                            Log.d(TAG, "Document exists!");
                                        } else {
                                            Map<String, Object> ACT = new HashMap<>();
                                            ACT.put("휴식 시간", 0);
                                            ACT.put("운동 시간", 0);
                                            userOfPet2.set(ACT);
                                            switch (Act){
                                                case "LYING" :
                                                case "SITDOWN" :
                                                    userOfPet2.update("휴식 시간", FieldValue.increment(1));
                                                    break;
                                                default :
                                                    userOfPet2.update("운동 시간", FieldValue.increment(1));
                                                    break;
                                            }
                                            Log.d(TAG, "Document does not exist!");
                                        }
                                    }
                                    else {
                                        Log.d(TAG, "Failed with: ", task.getException());
                                    }
                                });
                            }
                        }

                        client.close();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
