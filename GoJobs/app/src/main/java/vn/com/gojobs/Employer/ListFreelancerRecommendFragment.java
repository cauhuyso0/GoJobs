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
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class ListFreelancerRecommendFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "ListFreelancerRecommendFragment";
    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String accessTokenDb;
    String text;
    private int page = 0;
    private ArrayList<Freelancer> freelancerArrayList = new ArrayList<>();
    private CustomProgressBar customProgressBar;

    public ListFreelancerRecommendFragment(String text) {
        this.text = text;
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
        View view = inflater.inflate(R.layout.fragment_list_freelancer_recommend, container, false);

        customProgressBar = new CustomProgressBar(getContext());
        rlContain = view.findViewById(R.id.rl_job_freelancer_apply);
        if (_id != null) {
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        rvAllJobQuerryItem = (RecyclerView) view.findViewById(R.id.rvAllFreelancerQuerryItem);

        if (SearchEmployerFragment.dsFreelancerSearch.size() != 0) {
            freelancerArrayList.clear();
            freelancerArrayList.addAll(SearchEmployerFragment.dsFreelancerSearch);
            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvAllJobQuerryItem.setLayoutManager(linearLayoutManager);
            FreelancerAdapter adapter3 = new FreelancerAdapter(getContext(), freelancerArrayList , ListFreelancerRecommendFragment.this);
            rvAllJobQuerryItem.setAdapter(adapter3);
        } else {
            page++;
            getFlcPaginationWithSearch(text, page);
        }


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
                    page++;
                    getFlcPaginationWithSearch(text, page);
                } else {
                    LinearLayoutManager myLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int scrollPosition = myLayoutManager.findFirstCompletelyVisibleItemPosition();
                    Log.d("duc", "onScrollStateChanged: " + scrollPosition);
                    if (scrollPosition == 0){
                        page = 0;
                        freelancerArrayList.clear();
                        getFlcPaginationWithSearch(text,page);
                    }
                }
            }
        });

    }

    private void getFlcPaginationWithSearch(String textSearch, int page) {

        Log.d("duc", "getFlcPaginationWithSearch: page " + page + "freelancerArrayList.size : " + freelancerArrayList.size());

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getFlcPaginations(textSearch + "", GojobConfig.SORT_DESC, page, 4, accessTokenDb).enqueue(new Callback<List<Freelancer>>() {
            @Override
            public void onResponse(Call<List<Freelancer>> call, Response<List<Freelancer>> response) {
                List<Freelancer> freelancers = response.body();
                if (freelancers.size() != 0) {
                    freelancerArrayList.addAll(freelancers);
                    linearLayoutManager = new LinearLayoutManager(getActivity());
                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvAllJobQuerryItem.setLayoutManager(layoutManager3);
                    FreelancerAdapter adapter3 = new FreelancerAdapter(getContext(), freelancerArrayList, ListFreelancerRecommendFragment.this);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Freelancer>> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });

    }


//    void getFlcByEmpFollow(){
//        API api = retrofitClient.getClien().create(API.class);
//        api.getFlcByEmpFollow("empId").enqueue(new Callback<List<Follow>>() {
//            @Override
//            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
//                System.out.println("code Flc by emp follow: " + response.code());
//                List<Follow> follows = response.body();
//                System.out.println(follows);
//            }
//
//            @Override
//            public void onFailure(Call<List<Follow>> call, Throwable t) {
//                System.out.println("err");
//            }
//        });
//    }
//
//    void delFollowFlcByEmp(){
//        API api = retrofitClient.getClien().create(API.class);
//        api.delFollowFlcByEmp("empId", "flcId", "empId").enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                System.out.println("code del follow flc: " + response.code());
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