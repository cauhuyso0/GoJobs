package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;

public class ChangePasswordFragment extends Fragment {

    public static final String TAG = "ChangePasswordFragment";
    RetrofitClient retrofitClient = new RetrofitClient();
    EditText edt_old_password, edt_new_password, edtEmail;
    TextView tv_submit;
    String accessTokenDb;
    String mail = "";

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        edtEmail = view.findViewById(R.id.tv_email);
        edt_new_password = view.findViewById(R.id.edt_new_password);
        edt_old_password = view.findViewById(R.id.edt_old_password);
        tv_submit = view.findViewById(R.id.tv_submit);

        if (LoginEmployerFragment.accessTokenDb != null) {
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else if (LoginFreelancerFragment.accessTokenDb != null) {
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else if (AuthActivity.accessTokenDbFlc != null) {
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        } else if (AuthActivity.accessTokenDbEmp != null) {
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        // is freelancer
        if (LoginFreelancerFragment.flcEmail != null) {
            mail = LoginFreelancerFragment.flcEmail;
        } else if (AuthActivity.flcEmail != null) {
            mail = AuthActivity.flcEmail;
        }
        // is employer
        else if (LoginEmployerFragment.empEmail != null) {
            mail = LoginEmployerFragment.empEmail;
        } else if (AuthActivity.empEmail != null) {
            mail = AuthActivity.empEmail;
        }

        edtEmail.setText(mail);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String matKhauCu, matKhauMoi;

                matKhauCu = edt_old_password.getText().toString();
                matKhauMoi = edt_new_password.getText().toString();

                if (!matKhauCu.equals("")) {

                    if (!matKhauMoi.equals("")) {
                        if (AuthActivity.isEmployer || LoginEmployerFragment.isEmployer) {
                            updatePasswordEmp(mail, matKhauCu, matKhauMoi, accessTokenDb);
                        } else {
                            updatePasswordFlc(mail, matKhauCu, matKhauMoi, accessTokenDb);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Bạn cần nhập mật khẩu mới.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Bạn cần nhập mật khẩu hiện tại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    void updatePasswordEmp(String email, String password, String newPassword, String dbToken) {
        API api = retrofitClient.getClien().create(API.class);
        api.updatePasswordEmp(email + "", password + "", newPassword + "", dbToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Đổi password thành công!", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(getActivity(), "Mật khẩu cũ sai!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    void updatePasswordFlc(String email, String password, String newPassword, String dbToken) {
        API api = retrofitClient.getClien().create(API.class);
        api.updatePasswordFlc(email + "", password + "", newPassword + "", dbToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Đổi password thành công!", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(getActivity(), "Mật khẩu cũ sai!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }
}