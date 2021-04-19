package vn.com.gojobs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.FileNotFoundException;

import vn.com.gojobs.R;
import vn.com.gojobs.interfake.OnImageCallback;

import static vn.com.gojobs.R.id.tv_message_galary;

public class CaptureDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "CaptureDialog";
    private TextView tvCamera, tvGalary, tvDelete;
    private RelativeLayout bgDialog;
    private OnImageCallback mCallback;
    private Dialog root_dlg;


    public CaptureDialog(OnImageCallback onDialogCallback) {
        this.mCallback = onDialogCallback;
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

        View view = inflater.inflate(R.layout.dialog_capture, container);

        tvCamera = view.findViewById(R.id.tv_message_camera);
        tvCamera.setOnClickListener(this);
        tvGalary = view.findViewById(tv_message_galary);
        tvGalary.setOnClickListener(this);
        tvDelete = view.findViewById(R.id.tv_message_delete);
        tvDelete.setOnClickListener(this);

        bgDialog = view.findViewById(R.id.bg_dialog);
        bgDialog.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_message_camera:

                mCallback.takePicture();
                dismiss();
                break;

            case R.id.tv_message_galary:

                mCallback.takeGallery();
                dismiss();
                break;

            case R.id.tv_message_delete:

                mCallback.onRemove();
                dismiss();
                break;

            case R.id.bg_dialog:

                dismiss();
                break;

            default:
                break;
        }
    }
}
