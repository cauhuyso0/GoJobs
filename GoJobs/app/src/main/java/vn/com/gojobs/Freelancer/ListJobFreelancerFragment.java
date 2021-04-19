package vn.com.gojobs.Freelancer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Adapter.ListJobFreelancerAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Fragment.JobDetailFragment;
import vn.com.gojobs.Model.Follow;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;

public class ListJobFreelancerFragment extends Fragment implements IItemRowClickedCallback, View.OnClickListener {

    public static final String TAG = "ListJobFreelancerFragment";

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId;

    String status1 = GojobConfig.TAT_CA;
    String accessTokenDb;
    ArrayList<Job> jobs = new ArrayList<>();
    CustomProgressBar customProgressBar;

    public ListJobFreelancerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_job_freelancer, null);

        customProgressBar = new CustomProgressBar(getContext());
        rlContain = view.findViewById(R.id.rl_list_all_job_freelancer);
        rlContain.setOnClickListener(this);

        if (_id != null) {
            endFlcId = _id;
        } else {
            endFlcId = AuthActivity.flcId;
        }

        if (LoginEmployerFragment.accessTokenDb != null) {
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else if (LoginFreelancerFragment.accessTokenDb != null) {
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else if (AuthActivity.accessTokenDbFlc != null) {
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        } else if (AuthActivity.accessTokenDbEmp != null) {
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllJobQuerryItem = view.findViewById(R.id.rvAllFlcQuerryItem);
        rvAllJobQuerryItem.setLayoutManager(linearLayoutManager);

        getJobFreelancer();

        showNoneData();
        return view;
    }

    @Override
    public void onButtonClicked(String _id) {

        fragmentManager = getActivity().getSupportFragmentManager();
        JobDetailFragment jobDetailFragment = new JobDetailFragment(_id);
        fragmentManager.beginTransaction().replace(R.id.fragment_job, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();

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

    @Override
    public void onClick(View view) {
        fragmentManager = getFragmentManager();
        switch (view.getId()) {
            case R.id.rl_list_all_job_employer:
                break;
        }
    }

    private void getJobFreelancer() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getJobByFlcFollow(endFlcId + "", accessTokenDb).enqueue(new Callback<List<Follow>>() {
            @Override
            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
                List<Follow> follows = response.body();
                if (follows != null) {

                    for (Follow follow : follows) {
                        jobs.add(follow.getJobId());
                    }
                    ListJobFreelancerAdapter adapter3 = new ListJobFreelancerAdapter(getContext(), jobs, ListJobFreelancerFragment.this);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "Không có công việc.", Toast.LENGTH_SHORT).show();
                }

                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Follow>> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }
}
