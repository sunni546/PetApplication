package com.example.pet.utils;

import android.content.Context;

import androidx.annotation.StringRes;

public class StringResource{
    public static String getStringResource(Context context, @StringRes int id) {
        return context.getString(id);
    }
}
