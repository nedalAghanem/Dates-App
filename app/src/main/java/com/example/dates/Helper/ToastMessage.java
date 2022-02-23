package com.example.dates.Helper;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastMessage {

    public ToastMessage() {

    }

    public static com.example.dates.Helper.ToastMessage setMessage(Activity context, String message, int icon, int color) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        //Params TextView
        TextView txtToast = new TextView(context);
        txtToast.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtToast.setTextColor(Color.WHITE);
        //Params ImageView
        ImageView imageToast = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(40, 40);
        imageParams.setMarginEnd(18);
        imageToast.setLayoutParams(imageParams);
        // params linearLayout
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(35, 20, 35, 20);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(imageToast);
        linearLayout.addView(txtToast);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(linearLayout);
        toast.show();
        txtToast.setText(message);
        if (icon != 0)
            imageToast.setImageResource(icon);
        GradientDrawable drawable = new GradientDrawable();
        if (color != 0)
            drawable.setColor(color);
        drawable.setCornerRadius(50f);
        linearLayout.setBackground(drawable);

        return new com.example.dates.Helper.ToastMessage();
    }


}
