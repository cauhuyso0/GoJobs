package vn.com.gojobs.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import vn.com.gojobs.R;
import vn.com.gojobs.interfake.ITurnOnGPSCallback;

public class DialogNotification extends DialogFragment{

    public static final String TAG = "DialogNotification";
    private Dialog root_dlg;
    private String content;
    TextView tvContent;

    public DialogNotification(String content) {
        this.content = content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root_dlg = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        root_dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        root_dlg.setCanceledOnTouchOutside(true);
        root_dlg.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        root_dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        root_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        root_dlg.setContentView(R.layout.dialog_turn_on_gps);
        root_dlg.setCancelable(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                root_dlg.dismiss();
            }
        },1500);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return root_dlg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_notification,container);

        tvContent = view.findViewById(R.id.tv_content_notification);
        tvContent.setText(content);

        return view;
    }
}
