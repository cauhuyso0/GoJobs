package vn.com.gojobs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import vn.com.gojobs.R;

public class CustomProgressBar extends Dialog {

    public CustomProgressBar(Context context) {
        super(context);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
        setTitle("Đang tải dữ liệu ...");
        setCancelable(false);
        setOnCancelListener(null);
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_progress, null);
        setContentView(view);
    }
}
