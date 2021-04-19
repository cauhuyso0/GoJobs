package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import vn.com.gojobs.R;
import vn.com.gojobs.Freelancer.RegisterFreelancerFragment;

public class TermOfServiceFragment extends Fragment {

    public static final String TAG = "TermOfServiceFragment";
    Button btnTcBack;
    FragmentManager fragmentManager;

    public TermOfServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_tc,null);

        // ánh xạ
        btnTcBack = view.findViewById(R.id.btnTcBack);

        btnTcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                RegisterFreelancerFragment registerFreelancerFragment = new RegisterFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, registerFreelancerFragment).commit();

//                RegisterEmployerFragment registerEmployerFragment = new RegisterEmployerFragment();
//                fragmentManager.beginTransaction().replace(R.id.fragment_auth, registerEmployerFragment).commit();
            }
        });

        return view;

    }

}
