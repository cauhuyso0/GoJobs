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
import vn.com.gojobs.Adapter.EmployerAdapter;
import vn.com.gojobs.Adapter.FreelancerAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.CVFreelancerFragment;
import vn.com.gojobs.Model.Employer;
import vn.com.gojobs.Model.Follow;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;

public class ListEmployerFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "ListEmployerFragment";
    private ArrayList<Employer> employers = new ArrayList<>();

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId, accessTokenDb;

    public ListEmployerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_list_employer, container, false);

        if (_id != null) {
            endFlcId = _id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            endFlcId = AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }
        getEmpByFlcFollow();
        rlContain = view.findViewById(R.id.rl_list_employer_da_luu);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllJobQuerryItem = (RecyclerView) view.findViewById(R.id.rvAllFreelancerQuerryItem);

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

                } else {

                }
            }
        });
    }

    @Override
    public void onButtonClicked(String nameItem) {
        fragmentManager = getFragmentManager();
        CVFreelancerFragment cvFreelancerFragment = new CVFreelancerFragment(nameItem);
        fragmentManager.beginTransaction().replace(R.id.fragment_list_employer, cvFreelancerFragment).addToBackStack(CVFreelancerFragment.TAG).commit();
    }

    private void getEmpByFlcFollow() {
        API api = retrofitClient.getClien().create(API.class);
        api.getEmpByFlcFollow(endFlcId, accessTokenDb).enqueue(new Callback<List<Follow>>() {
            @Override
            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
                List<Follow> follows = response.body();
                if (follows != null) {
                    employers.clear();

                    for (Follow employer : follows){
                        employers.add(employer.getEmpId());
                    }

                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvAllJobQuerryItem.setLayoutManager(layoutManager3);
                    EmployerAdapter adapter3 = new EmployerAdapter(getContext(), employers);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                }
                else {
                    Toast.makeText(getActivity(), "Bạn chưa theo dõi Employer nào.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Follow>> call, Throwable t) {

            }
        });
    }
}