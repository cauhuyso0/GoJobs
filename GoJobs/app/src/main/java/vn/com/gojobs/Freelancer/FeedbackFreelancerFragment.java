package vn.com.gojobs.Freelancer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Adapter.FeedbackAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Model.Feedback;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;

public class FeedbackFreelancerFragment extends Fragment {

    public static final String TAG = "FeedbackFragment";
    RecyclerView recyclerViewFeedback;
    FeedbackAdapter feedbackAdapter;
    ArrayList<Feedback> dsFeedback;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId;
    String endEmpId;
    String accessTokenDb;
    private CustomProgressBar customProgressBar;

    public FeedbackFreelancerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_freelancer_feedback, container, false);

        customProgressBar = new CustomProgressBar(getContext());
//        dataDummy();
        recyclerViewFeedback = view.findViewById(R.id.rc_feedback);

        if (_id != null) {
            endFlcId = _id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            endFlcId = AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }
        if (LoginEmployerFragment._id != null) {
            endEmpId = LoginEmployerFragment._id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        if (endEmpId != null){
            getFeedbackByEmpId();
        } else {
            getFeedbackByFlcId();
        }


        return view;
    }


    void getFeedbackByFlcId() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getFeedbackByFlc(endFlcId, accessTokenDb).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                System.out.println("code get feedback by flcId " + response.code());
                List<Feedback> feedbacks = response.body();

                if (feedbacks != null) {

                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewFeedback.setLayoutManager(layoutManager3);
                    feedbackAdapter = new FeedbackAdapter(getContext(), (ArrayList<Feedback>) feedbacks);
                    recyclerViewFeedback.setAdapter(feedbackAdapter);
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                System.out.println("err " + t);
                customProgressBar.dismiss();
            }
        });
    }

    void getFeedbackByEmpId() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getFeedbackByEmp(endEmpId, accessTokenDb).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                System.out.println("code get feedback by flcId " + response.code());
                List<Feedback> feedbacks = response.body();
                if (feedbacks != null) {
                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewFeedback.setLayoutManager(layoutManager3);
                    feedbackAdapter = new FeedbackAdapter(getContext(), (ArrayList<Feedback>) feedbacks);
                    recyclerViewFeedback.setAdapter(feedbackAdapter);
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });
    }
}