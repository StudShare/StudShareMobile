package com.studshare.mobile.studshare.other;

import android.content.Context;
import android.widget.Toast;

public class ShowMessage {

    public static void Show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
