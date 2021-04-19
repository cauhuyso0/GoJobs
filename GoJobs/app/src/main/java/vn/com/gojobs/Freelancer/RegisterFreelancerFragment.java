package vn.com.gojobs.Freelancer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Fragment.TermOfServiceFragment;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.dialog.DialogNotification;
import vn.com.gojobs.interfake.API;

public class RegisterFreelancerFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "RegisterFreelancerFragment";
    private TextView tvFreelancerIntentSignin, tvTcFreelancer;
    private Button btnFreelancerSignup, btnBackFreelancerSignup;
    private EditText edtEmail, edtMatKhau;
    private CheckBox cbAccept;
    FragmentManager fragmentManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    private boolean isAcceptTermService;
    private FragmentTransaction fragmentTransaction;
    private CustomProgressBar customProgressBar;

    public RegisterFreelancerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_freelancer_register, null);
        customProgressBar = new CustomProgressBar(getContext());
        // map item
        initItem(view);
        //set onclick for item
        setOnClick();

        return view;
    }

    private void initItem(View view) {
        tvFreelancerIntentSignin = view.findViewById(R.id.tv_login_register_freelancer);
        tvTcFreelancer = view.findViewById(R.id.tv_term_service_freelancer);
        btnFreelancerSignup = view.findViewById(R.id.btn_continue_register_freelancer);
        btnBackFreelancerSignup = view.findViewById(R.id.btn_back_register_freelancer);
        edtMatKhau = view.findViewById(R.id.edt_register_password_freelancer);
        edtEmail = view.findViewById(R.id.edt_register_email_freelancer);
        cbAccept = view.findViewById(R.id.cb_accept);
    }

    private void setOnClick() {
        btnFreelancerSignup.setOnClickListener(this);
        btnBackFreelancerSignup.setOnClickListener(this);
        tvFreelancerIntentSignin.setOnClickListener(this);
        tvTcFreelancer.setOnClickListener(this);
        cbAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    isAcceptTermService = false;
                } else {
                    isAcceptTermService = true;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue_register_freelancer:

                if (isAcceptTermService) {

                    customProgressBar.show();
                    API api = retrofitClient.getClien().create(API.class);
                    api.flcRegister(edtEmail.getText().toString(),
                            edtMatKhau.getText().toString(),
                            true).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                // show dialog đăng kí thành công
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Đăng kí thành công.", Toast.LENGTH_SHORT).show();
                                // chuyển sang màn hình đăng nhập
                                fragmentManager = getFragmentManager();
                                LoginFreelancerFragment loginFreelancerFragment = new LoginFreelancerFragment();
                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, loginFreelancerFragment).commit();
                            } else if (response.code() == 404) {
                                // show dialog tài khoản đã tồn tại
                                customProgressBar.dismiss();
                                DialogNotification dialogNotification = new DialogNotification("Tài khoản đã tồn tại.");
                                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                            } else {
                                // show dialog đăng kí thất bại
                                customProgressBar.dismiss();
                                DialogNotification dialogNotification = new DialogNotification("Đăng kí thất bại.");
                                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // show dialog lỗi mạng
                            customProgressBar.dismiss();
                            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            DialogNotification dialogNotification = new DialogNotification("Gặp vấn đề về mạng.");
                            dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                        }
                    });
                } else {
                    // show dialog chấp nhận điều khoản
                    DialogNotification dialogNotification = new DialogNotification("Bạn cần chấp nhận điều khoản.");
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                }

                break;
            case R.id.btn_back_register_freelancer:
            case R.id.tv_login_register_freelancer:

                getActivity().getSupportFragmentManager().popBackStack();

                break;
            case R.id.tv_term_service_freelancer:

                fragmentManager = getFragmentManager();
                TermOfServiceFragment termOfServiceFragment = new TermOfServiceFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, termOfServiceFragment).addToBackStack(TermOfServiceFragment.TAG).commit();

                break;
        }
    }
}
