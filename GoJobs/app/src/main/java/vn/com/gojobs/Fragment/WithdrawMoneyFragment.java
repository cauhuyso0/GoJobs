package vn.com.gojobs.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.gojobs.R;

public class WithdrawMoneyFragment extends Fragment {

   public static final String TAG = "WithdrawMoneyFragment";
   TextView tvTiepTuc;
    private FragmentManager fragmentManager;

    public WithdrawMoneyFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw_money, container, false);

        tvTiepTuc = view.findViewById(R.id.tv_tiep_tuc_rut_tien);

        tvTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                OTPVerifyFragment otpVerifyFragment = new OTPVerifyFragment();
                fragmentManager.beginTransaction().replace(R.id.withdraw_money_fragment, otpVerifyFragment).addToBackStack(OTPVerifyFragment.TAG).commit();
            }
        });
        return view;
    }
}