package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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

public class OTPVerifyFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "OTPVerifyFragment";
    RetrofitClient retrofitClient = new RetrofitClient();
    private EditText edt1, edt2, edt3, edt4, edt5, edt6;
    private TextView tvCountDownTime, tvReVerify, tvXacNhan;
    String accessTokenDb;

    public OTPVerifyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_o_p_t_verify, container, false);

        edt1 = view.findViewById(R.id.edt1);
        edt2 = view.findViewById(R.id.edt2);
        edt3 = view.findViewById(R.id.edt3);
        edt4 = view.findViewById(R.id.edt4);
        edt5 = view.findViewById(R.id.edt5);
        edt6 = view.findViewById(R.id.edt6);
        tvCountDownTime = view.findViewById(R.id.tv_count_down_time);
        tvCountDownTime.setOnClickListener(this);
        tvReVerify = view.findViewById(R.id.tv_reverify);
        tvReVerify.setOnClickListener(this);
        tvXacNhan = view.findViewById(R.id.tv_xac_nhan_otp);
        tvXacNhan.setOnClickListener(this);
        if (LoginEmployerFragment.accessTokenDb != null){
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else if (LoginFreelancerFragment.accessTokenDb != null){
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else if (AuthActivity.accessTokenDbFlc != null){
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }else if (AuthActivity.accessTokenDbEmp != null){
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        setTextChange();
        return view;
    }

    private void setTextChange() {
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt1.getText().toString().length() == 1)     //size as per your requirement
                {
                    edt2.requestFocus();
                }
            }
        });

        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt2.getText().toString().length() == 1)     //size as per your requirement
                {
                    edt3.requestFocus();
                } else if (edt2.getText().toString().length() == 0) {
                    edt1.requestFocus();
                }
            }
        });

        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt3.getText().toString().length() == 1)     //size as per your requirement
                {
                    edt4.requestFocus();
                } else if (edt3.getText().toString().length() == 0) {
                    edt2.requestFocus();
                }
            }
        });

        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt4.getText().toString().length() == 1)     //size as per your requirement
                {
                    edt5.requestFocus();
                } else if (edt4.getText().toString().length() == 0) {
                    edt3.requestFocus();
                }
            }
        });

        edt5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt5.getText().toString().length() == 1) {
                    edt6.requestFocus();
                } else if (edt5.getText().toString().length() == 0) {
                    edt4.requestFocus();
                }
            }
        });

        edt6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt6.getText().toString().length() == 0) {
                    edt5.requestFocus();
                }
            }
        });
    }

    private void countTime() {

        new CountDownTimer(61000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountDownTime.setText(millisUntilFinished / 1000 + "");
                tvReVerify.setEnabled(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvCountDownTime.setText("0");
                tvReVerify.setEnabled(true);
            }

        }.start();
    }

    void getOTP() {
        API api = retrofitClient.getClien().create(API.class);
        api.getOTP("+84972670364", accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code get OTP " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    void verifyOTP(String maOTP) {
        API api = retrofitClient.getClien().create(API.class);
        api.verifyOTP("+84972670364", maOTP, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code verify OTP " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reverify:
                countTime();
                break;
            case R.id.tv_xac_nhan_otp:
                String num1 = edt1.getText().toString();
                String num2 = edt2.getText().toString();
                String num3 = edt3.getText().toString();
                String num4 = edt4.getText().toString();
                String num5 = edt5.getText().toString();
                String num6 = edt6.getText().toString();

                if (!num1.equals("") && !num2.equals("") && !num3.equals("") && !num4.equals("") && !num5.equals("") && !num6.equals("")) {
                    String maOTP = num1 + num2 + num3 + num4 + num5 + num6;
                    verifyOTP(maOTP);
                }else{
                    Toast.makeText(getActivity(), "Sai mã OTP, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}