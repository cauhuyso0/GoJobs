package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import vn.com.gojobs.R;

public class RechargeFragment extends Fragment {

    public static final String TAG = "RechargeFragment";
    TextView tvTiepTuc;
    private FragmentManager fragmentManager;

    public RechargeFragment() {
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

        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
//
//        tvTiepTuc = view.findViewById(R.id.tv_nap_tien_tk);
//
//        tvTiepTuc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragmentManager = getFragmentManager();
//                OTPVerifyFragment otpVerifyFragment = new OTPVerifyFragment();
//                fragmentManager.beginTransaction().replace(R.id.recharge_fragment, otpVerifyFragment).addToBackStack(OTPVerifyFragment.TAG).commit();
//            }
//        });
        return view;
    }
}