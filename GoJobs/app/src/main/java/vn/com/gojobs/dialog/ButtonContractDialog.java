package vn.com.gojobs.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Model.APIContract;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.onClickButton;

public class ButtonContractDialog extends DialogFragment implements View.OnClickListener {
    String _id, contractStatus;
    public static final String TAG = "DialogNotification";
    private Dialog root_dlg;
    private TextView tv_title, tv_huy, tv_xac_nhan;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endEmpId, accessTokenDb;
    CustomProgressBar customProgressBar;
    onClickButton onClickButton;
    FragmentManager fragmentManager;

    public ButtonContractDialog(String _id, String contractStatus, onClickButton onClickButton) {
        this._id = _id;
        this.contractStatus = contractStatus;
        this.onClickButton = onClickButton;
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

        View view = inflater.inflate(R.layout.button_contract_dialog, container);
        customProgressBar = new CustomProgressBar(getContext());
        if (LoginEmployerFragment._id != null) {
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        tv_huy = view.findViewById(R.id.tv_huy);
        tv_huy.setOnClickListener(this);
        tv_title = view.findViewById(R.id.tv_title);
        tv_xac_nhan = view.findViewById(R.id.tv_xac_nhan);
        tv_xac_nhan.setOnClickListener(this);
        Log.d("", "sssss: " + _id + "  " + contractStatus);

        if (contractStatus.equals(GojobConfig.STATUS_JOB_APPLIED)) {
            tv_title.setText("Bạn đã chấp nhận ứng viên này ?");
        } else if (contractStatus.equals(GojobConfig.STATUS_JOB_ACCEPTED)) {
            tv_title.setText("Bạn muốn bắt đầu công việc ?");
        } else if (contractStatus.equals(GojobConfig.STATUS_JOB_APPROVED)) {
            tv_title.setText("Công việc của bạn đã hoàn thành ?");
        } else if (contractStatus.equals(GojobConfig.STATUS_JOB_COMPLETED)) {
            tv_title.setText("Công việc đã hoàn thành !");
        } else if (contractStatus.equals(GojobConfig.STATUS_JOB_CANCELLED)) {
            tv_title.setText("Công việc của bạn đã hủy !");
        } else if (contractStatus.equals("CANCELED_JOB")) {
            tv_title.setText("Bạn muốn hủy và nhận lại 30% số tiền ?");
        } else if (contractStatus.equals("")) {
            tv_title.setText("Bạn muốn từ chối ứng viên này ?");
        }
        return view;
    }

    private void updateContracStatusACCEPTED() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.updateContractStatusACCEPTED(_id, GojobConfig.STATUS_JOB_ACCEPTED, endEmpId, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Ứng viên đã được chấp nhận!", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
                dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
            }
        });
    }

    private void updateContracStatusAPPROVED() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.updateContractStatusAPPROVED(_id, GojobConfig.STATUS_JOB_APPROVED, endEmpId, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Công việc đã được bắt đầu!", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
                dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
            }
        });
    }

    private void updateContracStatusCOMPLETED() {

        API api = retrofitClient.getClien().create(API.class);
        api.updateContractStatusCOMPLETED(_id, accessTokenDb).enqueue(new Callback<List<APIContract>>() {
            @Override
            public void onResponse(Call<List<APIContract>> call, Response<List<APIContract>> response) {

                List<APIContract> contract = response.body();
                Log.d("c", "contracts: " + contract);
                dismiss();
                if (contract != null) {
                    onClickButton.moveFeedback(contract.get(0).getFlcId(), contract.get(0).getJobId());
                }

            }

            @Override
            public void onFailure(Call<List<APIContract>> call, Throwable t) {
                Log.d("l", "erre " + t);

            }
        });


    }

    private void updateContracStatusCANCELLED() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.updateContractStatusCANCELLED(_id, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Công việc đã được hủy!", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
                dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
            }
        });
    }

    private void deleteContract() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.deleteContractById(_id, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Đã từ chối ứng viên!", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
                dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_xac_nhan:
                Log.d("click ", "clicked: ");
                if (contractStatus.equals(GojobConfig.STATUS_JOB_APPLIED)) {
                    updateContracStatusACCEPTED();
                } else if (contractStatus.equals(GojobConfig.STATUS_JOB_ACCEPTED)) {
                    updateContracStatusAPPROVED();
                } else if (contractStatus.equals(GojobConfig.STATUS_JOB_APPROVED)) {
                    updateContracStatusCOMPLETED();
                } else if (contractStatus.equals("CANCELED_JOB")) {
                    updateContracStatusCANCELLED();
                } else if (contractStatus.equals("")) {
                    deleteContract();
                }
                onClickButton.onAcceptDialog("Yes");

                break;
            case R.id.tv_huy:
                dismiss();
                break;
        }

    }
}
