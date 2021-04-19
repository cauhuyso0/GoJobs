package vn.com.gojobs.Freelancer;

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
import vn.com.gojobs.Fragment.SupportFragment;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;


public class MenuFreelancerFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MenuFreelancerFragment";
    RelativeLayout lLogOut_Freelancer, lChangePassword_Freelancer, lAppInfor_Freelancer, llSupport_Freelancer, lRecommend_Freelancer, lFeedback_Freelancer, lStatistic_Freelancer;
    LinearLayout lJobApplied, lJobSaved, lEmployerLiked;
    FragmentManager fragmentManager;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String KEY_EMAIL_FLC = "flcEmail";
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId;
    String accessTokenDb;


    public MenuFreelancerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_freelancer_menu, null);
        lJobApplied = view.findViewById(R.id.ln_quan_ly_cong_viec);
        lJobSaved = view.findViewById(R.id.ln_viec_da_luu_freelancer);
        lEmployerLiked = view.findViewById(R.id.ln_employer_yeu_thich);
        lStatistic_Freelancer = view.findViewById(R.id.rl_thong_ke_thu_nhap);
        lFeedback_Freelancer = view.findViewById(R.id.rl_danh_gia_freelancer);
        lRecommend_Freelancer = view.findViewById(R.id.rl_gioi_thieu_freelancer);
        llSupport_Freelancer = view.findViewById(R.id.rl_ho_tro_freelancer);
        lAppInfor_Freelancer = view.findViewById(R.id.rl_thong_tin_ung_dung_freelancer);
        lChangePassword_Freelancer = view.findViewById(R.id.rl_doi_mat_khau_freelancer);
        lLogOut_Freelancer = view.findViewById(R.id.rl_dang_xuat_freelancer);

        if (_id != null) {
            endFlcId = _id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            endFlcId = AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }

        itemClick();
        return view;
    }

    private void itemClick() {
        lJobApplied.setOnClickListener(this);
        lJobSaved.setOnClickListener(this);
        lEmployerLiked.setOnClickListener(this);
        lStatistic_Freelancer.setOnClickListener(this);
        lFeedback_Freelancer.setOnClickListener(this);
        lRecommend_Freelancer.setOnClickListener(this);
        llSupport_Freelancer.setOnClickListener(this);
        lAppInfor_Freelancer.setOnClickListener(this);
        lChangePassword_Freelancer.setOnClickListener(this);
        lLogOut_Freelancer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        fragmentManager = getFragmentManager();
        //check event onclick in this screen
        switch (view.getId()) {
            case R.id.ln_quan_ly_cong_viec:
                // thêm fragment giống như bên employer
                fragmentManager = getFragmentManager();
                FreelancerManagerJobFragment freelancerManagerJobFragment = new FreelancerManagerJobFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, freelancerManagerJobFragment).addToBackStack(FreelancerManagerJobFragment.TAG).commit();
                break;
            case R.id.ln_viec_da_luu_freelancer:
                fragmentManager = getFragmentManager();
                ListJobFreelancerFragment listJobFreelancerFragment = new ListJobFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, listJobFreelancerFragment).addToBackStack(ListJobFreelancerFragment.TAG).commit();
                break;
            case R.id.ln_employer_yeu_thich:
                fragmentManager = getFragmentManager();
                ListEmployerFragment listEmployerFragment = new ListEmployerFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, listEmployerFragment).addToBackStack(ListEmployerFragment.TAG).commit();
                break;
            case R.id.rl_thong_ke_thu_nhap:
                fragmentManager = getFragmentManager();
                FreelancerStatiticFragment freelancerStatiticFragment = new FreelancerStatiticFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, freelancerStatiticFragment).addToBackStack(FreelancerStatiticFragment.TAG).commit();
                break;
            case R.id.rl_danh_gia_freelancer:
                fragmentManager = getFragmentManager();
                FeedbackFreelancerFragment feedbackFreelancerFragment = new FeedbackFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, feedbackFreelancerFragment).addToBackStack(FeedbackFreelancerFragment.TAG).commit();
                break;
            case R.id.rl_gioi_thieu_freelancer:
                // introduce for friend, share link app
                break;
            case R.id.rl_ho_tro_freelancer:
                fragmentManager = getFragmentManager();
                SupportFragment supportFragment = new SupportFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, supportFragment).addToBackStack(SupportFragment.TAG).commit();
                break;
            case R.id.rl_thong_tin_ung_dung_freelancer:
                fragmentManager = getFragmentManager();
                AppInfoFragment appInfoFragment = new AppInfoFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, appInfoFragment).addToBackStack(AppInfoFragment.TAG).commit();
                break;
            case R.id.rl_doi_mat_khau_freelancer:
                fragmentManager = getFragmentManager();
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.frag_freelancer_menu, changePasswordFragment).addToBackStack(ChangePasswordFragment.TAG).commit();
                break;
            case R.id.rl_dang_xuat_freelancer:
                LoginFreelancerFragment._id = null;
                AccessToken.setCurrentAccessToken(null);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(KEY_EMAIL_FLC);
                editor.clear();
                editor.apply();
                GoogleSignInClient googleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                googleSignInClient.signOut();
                flcUpdateTokenDevice();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                LoginFreelancerFragment.isFreelancer = false;
                break;
        }
    }

    void flcUpdateTokenDevice() {
        API api = retrofitClient.getClien().create(API.class);
        api.flcUpdateTokenDevice(endFlcId, AuthActivity.tokenDevice, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code flcUpdateToken " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }
}
