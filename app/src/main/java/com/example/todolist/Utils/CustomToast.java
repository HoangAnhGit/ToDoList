package com.example.todolist.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;

public class CustomToast {

    public static void showCustomToast(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = layout.findViewById(R.id.tvToast);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

    public static void showCustomToastPlus(Context context, String message,int gravity,int resId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = layout.findViewById(R.id.tvToast);
        ImageView icon = layout.findViewById(R.id.icToast);
        text.setText(message);
        icon.setImageResource(resId);

        Toast toast = new Toast(context);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(gravity | Gravity.CENTER_HORIZONTAL , 0, 200);
        toast.show();
    }
}
