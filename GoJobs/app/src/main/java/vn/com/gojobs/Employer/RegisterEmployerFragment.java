package vn.com.gojobs.Employer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import vn.com.gojobs.R;
import vn.com.gojobs.Fragment.TermOfServiceFragment;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.dialog.DialogNotification;
import vn.com.gojobs.interfake.API;

public class RegisterEmployerFragment extends Fragment {

    public static final String TAG = "RegisterEmployerFragment";
    TextView tvEmployerIntentSignin, tvTcEmployer;
    Button btnEmployerSignup, btnBackEmployerSignup;
    FragmentManager fragmentManager;
    EditText edtEmail_EmployerSignup, edtPassword_EmployerSignup;
    CheckBox cbTc_Employer;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private CustomProgressBar customProgressBar;
    private FragmentTransaction fragmentTransaction;

    public RegisterEmployerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_employer_register, null);

        customProgressBar = new CustomProgressBar(getContext());
        // ánh xạ
        tvEmployerIntentSignin = view.findViewById(R.id.tvIntentSignin_Employer);
        tvTcEmployer = view.findViewById(R.id.tvTc_Employer);
        btnEmployerSignup = view.findViewById(R.id.btnSignup_Employer);
        btnBackEmployerSignup = view.findViewById(R.id.btnBackSignup_Employer);
        edtEmail_EmployerSignup = view.findViewById(R.id.edtEmail_EmployerSignup);
        edtPassword_EmployerSignup = view.findViewById(R.id.edtPassword_EmployerSignup);
        cbTc_Employer = view.findViewById(R.id.cbTc_Employer);

        btnEmployerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cbTc_Employer.isChecked()) {

                    String emailEmp = edtEmail_EmployerSignup.getText().toString();
                    String password = edtPassword_EmployerSignup.getText().toString();
                    customProgressBar.show();
                    API api = retrofitClient.getClien().create(API.class);
                    api.empRegister(emailEmp,password,true).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                                fragmentManager = getFragmentManager();
                                LoginEmployerFragment loginEmployerFragment = new LoginEmployerFragment();
                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, loginEmployerFragment).commit();
                            } else if (response.code() == 404) {
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Tài khoản đã tồn tại!", Toast.LENGTH_LONG).show();
                            } else {
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            customProgressBar.dismiss();
                            Toast.makeText(getActivity(), "Error" + t, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    DialogNotification dialogNotification = new DialogNotification("Bạn cần chấp nhận điều khoản.");
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    dialogNotification.show(fragmentTransaction, DialogNotification.TAG);
                }

            }
        });

        btnBackEmployerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        tvEmployerIntentSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        tvTcEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                TermOfServiceFragment termOfServiceFragment = new TermOfServiceFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, termOfServiceFragment).addToBackStack(TermOfServiceFragment.TAG).commit();
            }
        });

        return view;
    }
}
