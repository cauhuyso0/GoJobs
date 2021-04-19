package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Employer.MenuEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.Freelancer.MenuFreelancerFragment;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

public class FragmentGiveFeedback extends Fragment implements RatingBar.OnRatingBarChangeListener {

    public static final String TAG = "FragmentGiveFeedback";
    String userId;
    String jobId;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId, endEmpId;
    EditText fragFreelancerGiveFeedback_edtComment;
    TextView tvFeedbackSalary, fragFreelancerGiveFeedback_tvName;
    RatingBar rtFreelancerFeedback;
    Button send;
    String accessTokenDb;
    FragmentManager fragmentManager;
    CustomProgressBar customProgressBar;
    float star;
    String comment;

    public FragmentGiveFeedback(String userId, String jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_freelancer_give_feedback, container, false);
        customProgressBar = new CustomProgressBar(getContext());
        rtFreelancerFeedback = view.findViewById(R.id.rtFreelancerFeedback);
        rtFreelancerFeedback.setOnRatingBarChangeListener(this);
        fragFreelancerGiveFeedback_edtComment = view.findViewById(R.id.fragFreelancerGiveFeedback_edtComment);
        tvFeedbackSalary = view.findViewById(R.id.tvFeedbackSalary);
        fragFreelancerGiveFeedback_tvName = view.findViewById(R.id.fragFreelancerGiveFeedback_tvName);
        send = view.findViewById(R.id.btnSend);

        if (LoginFreelancerFragment._id != null){
            endFlcId = LoginFreelancerFragment._id;
        }else {
            endFlcId = AuthActivity.flcId;
        }

        if (LoginEmployerFragment.accessTokenDb != null){
            endEmpId = LoginEmployerFragment._id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else if (LoginFreelancerFragment.accessTokenDb != null){
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else if (AuthActivity.accessTokenDbFlc != null){
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }else if (AuthActivity.accessTokenDbEmp != null){
            endEmpId =  AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        getJobDetail();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endEmpId != null){
                    createEmpFeedback();
                    fragmentManager = getFragmentManager();
                    MenuEmployerFragment menuEmployerFragment = new MenuEmployerFragment();
                    fragmentManager.beginTransaction().replace(R.id.fl2, menuEmployerFragment).commit();
                }else {
                    createFlcFeedback();
                    fragmentManager = getFragmentManager();
                    MenuFreelancerFragment menuFreelancerFragment = new MenuFreelancerFragment();
                    fragmentManager.beginTransaction().replace(R.id.fl2, menuFreelancerFragment).commit();
                }

            }
        });

        return view;
    }


    private void createEmpFeedback(){
        comment = fragFreelancerGiveFeedback_edtComment.getText().toString();
        Log.d("feedback", "feedback: " + star + " " + comment);
        API api = retrofitClient.getClien().create(API.class);
        api.createEmpfeedback(endEmpId, userId, jobId, star, comment, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200){
                    Toast.makeText(getActivity(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("ere " + t);
                customProgressBar.dismiss();
            }
        });
    }

    private void createFlcFeedback(){
        API api = retrofitClient.getClien().create(API.class);
        api.createFlcfeedback(userId, endFlcId, jobId, rtFreelancerFeedback.getNumStars(), fragFreelancerGiveFeedback_edtComment.getText().toString(), accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200){
                    Toast.makeText(getActivity(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("ere " + t);
            }
        });
    }
    private void getJobDetail(){
        System.out.println();
        API api = retrofitClient.getClien().create(API.class);
        api.getJobDetail(jobId, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                List<Job> jobs = response.body();
                System.out.println(jobs);
                tvFeedbackSalary.setText(jobs.get(0).getJobTotalSalaryPerHeadCount()+"");
                fragFreelancerGiveFeedback_tvName.setText(jobs.get(0).getJobTitle());
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("rrrr" + t);
            }
        });
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        star = rating;
        Log.d("rating", "rating barr " + star);
    }
}