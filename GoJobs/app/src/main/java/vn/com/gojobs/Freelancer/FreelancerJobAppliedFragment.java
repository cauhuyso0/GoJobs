package vn.com.gojobs.Freelancer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Adapter.JobAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.JobDetailFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class FreelancerJobAppliedFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "FreelancerJobAppliedFragment";
    private ArrayList<Job> jobs;

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    private CustomProgressBar customProgressBar;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private String endFlcId;
    String accessTokenDb;

    public FreelancerJobAppliedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_freelancer_job_applied, container, false);

        customProgressBar = new CustomProgressBar(getContext());
        rlContain = view.findViewById(R.id.rl_freelancer_job_applied);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllJobQuerryItem = view.findViewById(R.id.rvAllFlcQuerryItem);
        rvAllJobQuerryItem.setLayoutManager(linearLayoutManager);

        if (LoginFreelancerFragment._id != null) {
            endFlcId = LoginFreelancerFragment._id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            endFlcId = AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getJobByContractStatus(endFlcId, GojobConfig.STATUS_JOB_APPLIED + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                Log.d("test", "onResponse: " + response.body());
                Log.d("test", "onResponse: " + endFlcId);


                List<Job> allContracts = response.body();

                JobAdapter adapter3 = new JobAdapter(getContext(), (ArrayList<Job>) allContracts, FreelancerJobAppliedFragment.this);
                rvAllJobQuerryItem.setAdapter(adapter3);

                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });

        showNoneData();

        return view;
    }

    @Override
    public void onButtonClicked(String _id) {

        fragmentManager = getActivity().getSupportFragmentManager();
        JobDetailFragment jobDetailFragment = new JobDetailFragment(_id);
        fragmentManager.beginTransaction().replace(R.id.rl_freelancer_job_applied, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();

    }

    private void showNoneData() {

        rvAllJobQuerryItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----", "end");

                } else {

                }
            }
        });
    }
}