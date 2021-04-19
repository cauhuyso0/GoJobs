package vn.com.gojobs.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import vn.com.gojobs.Adapter.JobAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.ListContractJobFragment;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class ListJobFragment extends Fragment implements IItemRowClickedCallback, View.OnClickListener { //onClickButton

    public static final String TAG = "ListJobFragment";

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    private ImageView imgAddJob;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endEmpId;
    Spinner spinner;
    private String[] status = {GojobConfig.TAT_CA, GojobConfig.CO_UNG_VIEN, GojobConfig.DA_CHAP_NHAN, GojobConfig.DANG_DIEN_RA, GojobConfig.DA_XONG, GojobConfig.DA_HUY};
    boolean notStatus = false;
    String status1 = GojobConfig.TAT_CA;
    String accessTokenDb;
    String statusJob = "";
    ArrayList<Job> jobs = new ArrayList<>();
    CustomProgressBar customProgressBar;

    public ListJobFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_job, null);

        customProgressBar = new CustomProgressBar(getContext());
        rlContain = view.findViewById(R.id.rl_list_all_job_employer);
        rlContain.setOnClickListener(this);
        imgAddJob = view.findViewById(R.id.btn_add_job);
        spinner = view.findViewById(R.id.spinner_status);
        customProgressBar = new CustomProgressBar(getContext());

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, status);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        if (_id != null) {
            endEmpId = _id;
        } else {
            endEmpId = AuthActivity.empId;
        }
        if (LoginFreelancerFragment.isFreelancer) {
            imgAddJob.setVisibility(View.GONE);
        }
        imgAddJob.setOnClickListener(this);

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
        getAllJob();
        getJobBaseSpinner();

        showNoneData();
        return view;
    }

    @Override
    public void onButtonClicked(String _id) {

        if (status1.equals(GojobConfig.TAT_CA)) {
            fragmentManager = getActivity().getSupportFragmentManager();
            JobDetailFragment jobDetailFragment = new JobDetailFragment(_id);
            fragmentManager.beginTransaction().replace(R.id.fragment_job, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();

        } else {
            fragmentManager = getActivity().getSupportFragmentManager();
            ListContractJobFragment listContractJobFragment = new ListContractJobFragment(_id, statusJob);
            fragmentManager.beginTransaction().replace(R.id.fragment_job, listContractJobFragment).addToBackStack(ListContractJobFragment.TAG).commit();

        }
    }

    private void showNoneData() {

        rvAllJobQuerryItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----", "end");
                    getJobBaseSpinner();
                } else {

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        fragmentManager = getFragmentManager();
        switch (view.getId()) {
            case R.id.btn_add_job:
                fragmentManager = getActivity().getSupportFragmentManager();
                AddJobFragment addJobFragment = new AddJobFragment();
                fragmentManager.beginTransaction().replace(R.id.rl_list_all_job_employer, addJobFragment).addToBackStack(AddJobFragment.TAG).commit();
                break;

            case R.id.rl_list_all_job_employer:
                break;
        }
    }

    private void getJobBaseSpinner() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status1 = spinner.getSelectedItem().toString();
                if (status1.equals(GojobConfig.TAT_CA)) {
                    getAllJob();
                    imgAddJob.setVisibility(View.VISIBLE);
                } else {

                    if (status1.equals(GojobConfig.CO_UNG_VIEN)) {
                        statusJob = GojobConfig.STATUS_JOB_APPLIED;
                    } else if (status1.equals(GojobConfig.DA_CHAP_NHAN)) {
                        statusJob = GojobConfig.STATUS_JOB_ACCEPTED;
                    } else if (status1.equals(GojobConfig.DA_XONG)) {
                        statusJob = GojobConfig.STATUS_JOB_COMPLETED;
                    } else if (status1.equals(GojobConfig.DANG_DIEN_RA)) {
                        statusJob = GojobConfig.STATUS_JOB_APPROVED;
                    } else if (status1.equals(GojobConfig.DA_HUY)) {
                        statusJob = GojobConfig.STATUS_JOB_CANCELLED;
                    }

                    imgAddJob.setVisibility(View.GONE);
                    customProgressBar.show();
                    API api = retrofitClient.getClien().create(API.class);
                    api.getJobByContractStatus(endEmpId, statusJob, 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                        @Override
                        public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                            jobs.clear();
                            List<Job> jobss = (ArrayList<Job>) response.body();

                            if (jobss != null) {
                                jobs.addAll(jobss);
                                JobAdapter adapter3 = new JobAdapter(getContext(), jobs, ListJobFragment.this);
                                rvAllJobQuerryItem.setAdapter(adapter3);
                                adapter3.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "Không có công việc " + status1, Toast.LENGTH_SHORT).show();
                            }

                            customProgressBar.dismiss();
                        }

                        @Override
                        public void onFailure(Call<List<Job>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getAllJob() {
        final ArrayList<Job> jobArrayList = new ArrayList<>();

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getJobByEmpId(endEmpId, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                List<Job> jobs = response.body();
                jobArrayList.addAll(jobs);

                if (jobs != null) {
                    JobAdapter adapter3 = new JobAdapter(getContext(), jobArrayList, ListJobFragment.this);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Bạn chưa tạo công việc!", Toast.LENGTH_SHORT).show();
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("eee " + t);
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");
        getAllJob();
        getJobBaseSpinner();
    }

//    @Override
//    public void onAcceptDialog(String status) {
//
//    }
//
//    @Override
//    public void moveFeedback(String flcId, String jobId) {
//
//    }
}
