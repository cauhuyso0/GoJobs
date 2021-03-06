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
                                // show dialog ????ng k?? th??nh c??ng
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "????ng k?? th??nh c??ng.", Toast.LENGTH_SHORT).show();
                                // chuy???n sang m??n h??nh ????ng nh???p
                                fragmentManager = getFragmentManager();
                                LoginFreelancerFragment loginFreelancerFragment = new LoginFreelancerFragment();
                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, loginFreelancerFragment).commit();
                            } else if (response.code() == 404) {
                                // show dialog t??i kho???n ???? t???n t???i
                                customProgressBar.dismiss();
                                DialogNotification dialogNotification = new DialogNotification("T??i kho???n ???? t???n t???i.");
                                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                            } else {
                                // show dialog ????ng k?? th???t b???i
                                customProgressBar.dismiss();
                                DialogNotification dialogNotification = new DialogNotification("????ng k?? th???t b???i.");
                                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // show dialog l???i m???ng
                            customProgressBar.dismiss();
                            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            DialogNotification dialogNotification = new DialogNotification("G???p v???n ????? v??? m???ng.");
                            dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                        }
                    });
                } else {
                    // show dialog ch???p nh???n ??i???u kho???n
                    DialogNotification dialogNotification = new DialogNotification("B???n c???n ch???p nh???n ??i???u kho???n.");
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
