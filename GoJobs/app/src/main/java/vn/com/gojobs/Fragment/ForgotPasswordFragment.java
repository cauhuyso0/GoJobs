package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.dialog.DialogNotification;
import vn.com.gojobs.interfake.API;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ForgotPasswordFragment";
    Button btnSendEmail;
    TextView tvBackForgotPassword;
    EditText edtEmailForgotPasswod;
    RetrofitClient retrofitClient = new RetrofitClient();
    private CustomProgressBar customProgressBar;
    private FragmentTransaction fragmentTransaction;
    String accessTokenDb;

    public ForgotPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_forgot_password, null);

        customProgressBar = new CustomProgressBar(getContext());
        if (LoginEmployerFragment.accessTokenDb != null) {
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else if (LoginFreelancerFragment.accessTokenDb != null) {
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else if (AuthActivity.accessTokenDbFlc != null) {
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        } else if (AuthActivity.accessTokenDbEmp != null) {
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        // ánh xạ
        initItem(view);
        // set onclick item
        setOnClick();

        return view;

    }

    private void initItem(View view) {
        btnSendEmail = view.findViewById(R.id.btn_send_mail);
        tvBackForgotPassword = view.findViewById(R.id.tv_back_forgot_password);
        edtEmailForgotPasswod = view.findViewById(R.id.edt_email_forgot_password);
    }

    private void setOnClick() {
        btnSendEmail.setOnClickListener(this);
        tvBackForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back_forgot_password:

                getActivity().getSupportFragmentManager().popBackStack();

                break;

            case R.id.btn_send_mail:

                final String email = edtEmailForgotPasswod.getText().toString();

                if (!email.equals("")) {

                    customProgressBar.show();

                    if (AuthActivity.isEmployer) {
                        API api = retrofitClient.getClien().create(API.class);
                        api.empSendEmailRePassword(email).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {

                                    DialogNotification dialogNotification = new DialogNotification("Email chứa liên kết đặt lại mật khẩu đã được gửi cho bạn. Nếu bạn không nhận được email sau vài phút, vui lòng kiểm tra thư mục thư rác!");
                                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    dialogNotification.show(fragmentTransaction, DialogNotification.TAG);

                                } else if (response.code() == 404) {

                                    Toast.makeText(getActivity(), "Email không tồn tại !", Toast.LENGTH_SHORT).show();
                                }

                                customProgressBar.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        API api = retrofitClient.getClien().create(API.class);
                        api.flcSendEmailRePassword(email).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {

                                    DialogNotification dialogNotification = new DialogNotification("Email chứa liên kết đặt lại mật khẩu đã được gửi cho bạn. Nếu bạn không nhận được email sau vài phút, vui lòng kiểm tra thư mục thư rác!");
                                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    dialogNotification.show(fragmentTransaction, DialogNotification.TAG);

                                } else if (response.code() == 404) {

                                    Toast.makeText(getActivity(), "Email không tồn tại !", Toast.LENGTH_SHORT).show();
                                }

                                customProgressBar.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Bạn cần nhập Email.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
