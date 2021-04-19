package vn.com.gojobs.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import vn.com.gojobs.interfake.ITurnOnGPSCallback;
import vn.com.gojobs.R;

public class DialogTurnOnGPS extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "DialogTurnOnGPS";
    private TextView tvAccept;
    private LinearLayout bgDialog;
    private ITurnOnGPSCallback iTurnOnGPSCallback;
    private Dialog root_dlg;

    public DialogTurnOnGPS(ITurnOnGPSCallback iTurnOnGPSCallback) {
        this.iTurnOnGPSCallback = iTurnOnGPSCallback;
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

        View view = inflater.inflate(R.layout.dialog_turn_on_gps,container);

        tvAccept = view.findViewById(R.id.tv_action_ok);
        tvAccept.setOnClickListener(this);

        bgDialog = view.findViewById(R.id.bg_dialog);
        bgDialog.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_action_ok:
                iTurnOnGPSCallback.turnOnGPS();
                dismiss();
            case R.id.bg_dialog:
                dismiss();
                break;
        }
    }
}
