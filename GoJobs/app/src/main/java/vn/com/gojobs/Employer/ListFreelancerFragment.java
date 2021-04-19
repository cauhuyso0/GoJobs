package vn.com.gojobs.Employer;

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
import vn.com.gojobs.Adapter.FreelancerAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Model.Follow;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class ListFreelancerFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "ListFreelancerFragment";
    private ArrayList<Freelancer> freelancers;

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endEmpId;
    String accessTokenDb;

    public ListFreelancerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_list_freelancer, container, false);

        rlContain = view.findViewById(R.id.rl_job_freelancer_apply);
        if (_id != null){
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        System.out.println(endEmpId + "empId");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllJobQuerryItem = (RecyclerView) view.findViewById(R.id.rvAllFreelancerQuerryItem);
        getFlcByEmpFollow();
        showNoneData();
        return view;
    }

    private void showNoneData() {

        rvAllJobQuerryItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----", "end");

                }else {

                }
            }
        });
    }
    
    void getFlcByEmpFollow(){
        final ArrayList<Freelancer> freelancerArrayList = new ArrayList<>();
        API api = retrofitClient.getClien().create(API.class);
        api.getFlcByEmpFollow(endEmpId, accessTokenDb).enqueue(new Callback<List<Follow>>() {
            @Override
            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
                System.out.println("code Flc by emp follow: " + response.code());
                List<Follow> follows = response.body();
                Log.d("a", "foloo " + follows);

                if (follows != null){
                    for (int i = 0; i < follows.size(); i++){
                        freelancerArrayList.add(follows.get(i).getFlcId());
                    }
                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvAllJobQuerryItem.setLayoutManager(layoutManager3);
                    FreelancerAdapter adapter3 = new FreelancerAdapter(getContext(), freelancerArrayList, ListFreelancerFragment.this);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                }

            }

            @Override
            public void onFailure(Call<List<Follow>> call, Throwable t) {
                System.out.println("err" + t);
            }
        });
    }

//    void delFollowFlcByEmp(){
//        API api = retrofitClient.getClien().create(API.class);
//        api.delFollowFlcByEmp("empId", "flcId", "empId").enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                System.out.println("code del follow flc: " + response.code());
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                System.out.println("err");
//            }
//        });
//    }

    @Override
    public void onButtonClicked(String nameItem) {
        fragmentManager = getFragmentManager();
        CVFreelancerFragment cvFreelancerFragment = new CVFreelancerFragment(nameItem);
        fragmentManager.beginTransaction().replace(R.id.fragment_list_freelancer, cvFreelancerFragment).addToBackStack(CVFreelancerFragment.TAG).commit();
    }
}