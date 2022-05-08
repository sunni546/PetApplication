package com.example.pet;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class DB_Ab {
    CollectionReference AbRef;
    String documentName;
    private int y = 0;

    // 생성자
    public DB_Ab(CollectionReference AbRef, String documentName) {
        this.AbRef = AbRef;
        this.documentName = documentName;
    }

    public int findY() {
        AbRef.document(documentName)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("GGGGGGGGGGGGGGGGGGGGG", String.valueOf(document.get("count")));
                    int count = Integer.parseInt(String.valueOf(document.get("count")));
                    Log.d("GGGGGGGGGGGGGGGGGGGGG", String.valueOf(count));
                    setY(count);
                    Log.d("GGGGGGGGGGGGGGGGGGGGG", String.valueOf(this.y));
                }
            }
        });

        return getY();
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
