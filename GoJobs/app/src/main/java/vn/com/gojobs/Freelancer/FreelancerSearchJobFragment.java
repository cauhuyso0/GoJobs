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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class FreelancerSearchJobFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "FreelancerSearchJobFragment";
    private ArrayList<Job> jobs;

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    String textSearch;
    RetrofitClient retrofitClient = new RetrofitClient();
    private ArrayList<Job> allJobs;
    String accessTokenDb;

    public FreelancerSearchJobFragment(String textSearch) {
        this.textSearch = textSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.freelancer_search_job_fragment, container, false);


        Log.d("test", "onCreateView:" + textSearch+"abc");
//        dataDummy();
        rlContain = view.findViewById(R.id.rl_freelancer_search_job);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllJobQuerryItem = view.findViewById(R.id.rvAllFlcQuerryItem);
        rvAllJobQuerryItem.setLayoutManager(linearLayoutManager);
        if (LoginFreelancerFragment.accessTokenDb != null){
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else {
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }

        getPaginationJobWithSearch(textSearch);

        showNoneData();

        return view;
    }

    void getPaginationJobWithSearch(String textSearch) {


        API api = retrofitClient.getClien().create(API.class);
        api.getJobWithSearch(textSearch + "", GojobConfig.SORT_DESC, 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                List<Job> jobs = response.body();
                System.out.println("jobs: " + jobs);

                allJobs = (ArrayList<Job>) response.body();
                if (allJobs.size() > 0) {
                    JobAdapter adapter3 = new JobAdapter(getContext(), allJobs, FreelancerSearchJobFragment.this);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                } else {
                    Toast.makeText(getActivity(), "Chưa có công việc bạn cần tìm.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    private void dataDummy() {
        // get data from api to show UI
        final String DATE_FORMAT = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("EN"));
        Date today = Calendar.getInstance().getTime();
        String date0 = dateFormat.format(today);
        jobs = new ArrayList<>();
        // All Job Item
        jobs.add(new Job("Android dev", "Người in hồ sơ", 70000, date0, date0, "on-going"));
        jobs.add(new Job("QC", "Lái ba gác", 70000, date0, date0, "on-going"));
        jobs.add(new Job("QA", "Lái xe bò", 70000, date0, date0, "on-going"));
        jobs.add(new Job("BRSI", "Phân loại tài liệu", 70000, date0, date0, "on-going"));
        jobs.add(new Job("Java", "Pha chế", 70000, date0, date0, "on-going"));
        jobs.add(new Job(".Net", "Phục vụ", 70000, date0, date0, "on-going"));
    }

    @Override
    public void onButtonClicked(String _id) {
        switch (_id) {
            case JobDetailFragment.TAG:
                fragmentManager = getActivity().getSupportFragmentManager();
                JobDetailFragment jobDetailFragment = new JobDetailFragment(_id);
                fragmentManager.beginTransaction().replace(R.id.fragment_job, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();
                break;
        }
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