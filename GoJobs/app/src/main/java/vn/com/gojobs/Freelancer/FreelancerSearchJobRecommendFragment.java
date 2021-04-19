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
import vn.com.gojobs.Fragment.JobDetailFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class FreelancerSearchJobRecommendFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "FreelancerSearchJobRecommendFragment";

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    List<String> list;
    CustomProgressBar customProgressBar;
    RetrofitClient retrofitClient = new RetrofitClient();
    String accessTokenDb;

    public FreelancerSearchJobRecommendFragment(List<String> list) {
        // Required empty public constructor
        this.list = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.freelancer_search_job_recommend_fragment, container, false);
        customProgressBar = new CustomProgressBar(getContext());

        rlContain = view.findViewById(R.id.rl_freelancer_search_recommend_job);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllJobQuerryItem = view.findViewById(R.id.rvAllFlcQuerryItem);
        rvAllJobQuerryItem.setLayoutManager(linearLayoutManager);
        if (LoginFreelancerFragment.accessTokenDb != null) {
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }

        API api = retrofitClient.getClien().create(API.class);
        if (list.size() > 0) {
            api.getJobWithFilter(GojobConfig.SORT_DESC + "", list, 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    List<Job> jobsRecommend = response.body();

                    if (jobsRecommend != null) {
                        JobAdapter adapter = new JobAdapter(getContext(), (ArrayList<Job>) jobsRecommend, FreelancerSearchJobRecommendFragment.this);
                        rvAllJobQuerryItem.setAdapter(adapter);
                    }
                    customProgressBar.dismiss();
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    System.out.println("err");
                    customProgressBar.dismiss();
                }
            });
        } else {
            api.getJobWithTime(GojobConfig.SORT_DESC + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                    List<Job> allJobs = response.body();

                    if (allJobs != null) {
                        JobAdapter adapter = new JobAdapter(getContext(), (ArrayList<Job>) allJobs, FreelancerSearchJobRecommendFragment.this);
                        rvAllJobQuerryItem.setAdapter(adapter);
                    }
                    customProgressBar.dismiss();

                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    System.out.println("err");
                    customProgressBar.dismiss();
                }
            });
        }

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
}