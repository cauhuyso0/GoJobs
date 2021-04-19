package vn.com.gojobs.Employer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.AppInfoFragment;
import vn.com.gojobs.Fragment.ChangePasswordFragment;
import vn.com.gojobs.Fragment.GojobSupportMapFragment;
import vn.com.gojobs.Fragment.ListJobFragment;
import vn.com.gojobs.Freelancer.FeedbackFreelancerFragment;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;


public class MenuEmployerFragment extends Fragment implements  View.OnClickListener{

    public static final String TAG = "MenuEmployerFragment";
    RelativeLayout lLogOutEmplyer, lChangePasswordEmployer, lAppInforEmployer, llSupportEmployer, lRecommendEmployer, lFeedbackEmployer, lStatisticEmployer;
    LinearLayout lJobPosted, lCVSaved;
    FragmentManager fragmentManager;
    private static final String SHARE_PREF_NAME = "mypref";
    private final String KEY_ID_EMP = "_id";
    private final String KEY_NAME_EMP = "empName";
    private static final String KEY_EMAIL_EMP = "empEmail";

    String endEmpId;
    RetrofitClient retrofitClient = new RetrofitClient();
    String accessTokenDb;

    public MenuEmployerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_employer_menu,null);
        lJobPosted = view.findViewById(R.id.llJobPosted);
        lCVSaved = view.findViewById(R.id.llCVSaved);
        lStatisticEmployer = view.findViewById(R.id.llStatistic_Employer);
        lFeedbackEmployer = view.findViewById(R.id.llFeedback_Employer);
        lRecommendEmployer = view.findViewById(R.id.llRecommend_Employer);
        llSupportEmployer = view.findViewById(R.id.llSupport_Employer);
        lAppInforEmployer = view.findViewById(R.id.llAppInfor_Employer);
        lChangePasswordEmployer = view.findViewById(R.id.llChangePassword_Employer);
        lLogOutEmplyer = view.findViewById(R.id.llLogOut_Employer);
        if (_id != null){
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        itemClick();
        return view;
    }

    private void itemClick() {
        lJobPosted.setOnClickListener(this);
        lCVSaved.setOnClickListener(this);
        lStatisticEmployer.setOnClickListener(this);
        lFeedbackEmployer.setOnClickListener(this);
        lRecommendEmployer.setOnClickListener(this);
        llSupportEmployer.setOnClickListener(this);
        lAppInforEmployer.setOnClickListener(this);
        lChangePasswordEmployer.setOnClickListener(this);
        lLogOutEmplyer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        fragmentManager = getFragmentManager();
        //check event onclick in this screen
        switch (view.getId()) {

            case R.id.llJobPosted:
                //thêm job đã hoàn thành -- apply tương tự như freelancer
                fragmentManager = getFragmentManager();
                ListJobFragment listJobFragment = new ListJobFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, listJobFragment).addToBackStack(ListJobFragment.TAG).commit();

                break;
            case R.id.llCVSaved:
                fragmentManager = getFragmentManager();
                ListFreelancerFragment listFreelancerFragment = new ListFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, listFreelancerFragment).addToBackStack(ListFreelancerFragment.TAG).commit();

                break;
            case R.id.llStatistic_Employer:
                // thống kê bao gồm ví và số dư
                fragmentManager = getFragmentManager();
                EmployerStatiticFragment employerStatiticFragment = new EmployerStatiticFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, employerStatiticFragment).addToBackStack(EmployerStatiticFragment.TAG).commit();

                break;
            case R.id.llFeedback_Employer:
                fragmentManager = getFragmentManager();
                FeedbackFreelancerFragment feedbackFreelancerFragment = new FeedbackFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, feedbackFreelancerFragment).addToBackStack(FeedbackFreelancerFragment.TAG).commit();
                break;
            case R.id.llRecommend_Employer:

                break;
            case R.id.llSupport_Employer:
                // hướng dẫn nạp tiền và apply free
                fragmentManager = getFragmentManager();
                GojobSupportMapFragment gojobSupportMapFragment = new GojobSupportMapFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, gojobSupportMapFragment).addToBackStack(GojobSupportMapFragment.TAG).commit();
                break;
            case R.id.llAppInfor_Employer:
                fragmentManager = getFragmentManager();
                AppInfoFragment appInfoFragment = new AppInfoFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, appInfoFragment).addToBackStack(AppInfoFragment.TAG).commit();
                break;
            case R.id.llChangePassword_Employer:
                fragmentManager = getFragmentManager();
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_employer_menu, changePasswordFragment).addToBackStack(ChangePasswordFragment.TAG).commit();
                break;
            case R.id.llLogOut_Employer:
                LoginEmployerFragment._id = null;
                AccessToken.setCurrentAccessToken(null);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_EMAIL_EMP,null);
                editor.putString(KEY_ID_EMP, null);
                editor.putString(KEY_NAME_EMP, null);
                editor.apply();
                GoogleSignInClient googleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                googleSignInClient.signOut();
                empUpdateTokenDevice();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                AuthActivity.isEmployer = false;
                LoginEmployerFragment.isEmployer = false;
//                fragmentManager.beginTransaction().replace(R.id.main_freelancer, loginFreelancerFragment).commit();
                break;
        }
    }

    void empUpdateTokenDevice(){
        API api = retrofitClient.getClien().create(API.class);
        api.empUpdateTokenDevice(endEmpId, AuthActivity.tokenDevice, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code empUpdateTokenDevice " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }
}
