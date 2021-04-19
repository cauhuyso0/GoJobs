package vn.com.gojobs.Employer;

import android.os.Bundle;
import android.os.Handler;
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
import vn.com.gojobs.Adapter.ContractAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.FragmentGiveFeedback;
import vn.com.gojobs.Model.Contract;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.ButtonContractDialog;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;
import vn.com.gojobs.interfake.ItemRowClickedCallbackForContract;
import vn.com.gojobs.interfake.onClickButton;

public class ListContractJobFragment extends Fragment implements IItemRowClickedCallback, ItemRowClickedCallbackForContract, onClickButton {

    public static final String TAG = "ListContractJobFragment";
    private final String _id;
    private String contractStatus;

    private ArrayList<Contract> contracts;

    FragmentManager fragmentManager;
    private RecyclerView rvAllContract;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String accessTokenDb;
    CustomProgressBar customProgressBar;

    public ListContractJobFragment(String _id, String contractStatus) {
        this._id = _id;
        this.contractStatus = contractStatus;
    }

    public ListContractJobFragment(String _id) {
        this._id = _id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_contract_job, container, false);
        customProgressBar = new CustomProgressBar(getContext());
        //dataDummy();
        rlContain = view.findViewById(R.id.rl_list_contract_fragment);
        rlContain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        rvAllContract = view.findViewById(R.id.rvAllContract);
        if (LoginEmployerFragment.accessTokenDb != null) {

            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {

            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        getContractByJobId();

        return view;
    }

    @Override
    public void onButtonClicked(String IdFlc) {

        fragmentManager = getActivity().getSupportFragmentManager();
        CVFreelancerFragment cvFreelancerFragment = new CVFreelancerFragment(IdFlc);
        fragmentManager.beginTransaction().replace(R.id.rl_list_contract_fragment, cvFreelancerFragment).addToBackStack(CVFreelancerFragment.TAG).commit();

    }


    private void getContractByJobId() {
        customProgressBar.show();
        final ArrayList<Contract> contractArrayList = new ArrayList<>();
        API api = retrofitClient.getClien().create(API.class);
        api.getContractByJobId(_id, contractStatus, accessTokenDb).enqueue(new Callback<List<Contract>>() {
            @Override
            public void onResponse(Call<List<Contract>> call, Response<List<Contract>> response) {
                List<Contract> contracts = response.body();
                Log.d("b", "ssss " + contracts);
                if (response.code() == 200) {
                    contractArrayList.clear();
                    contractArrayList.addAll(contracts);
                    if (contractArrayList != null) {
                        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvAllContract.setLayoutManager(layoutManager3);
                        ContractAdapter adapter3 = new ContractAdapter(getContext(), contractArrayList, ListContractJobFragment.this, ListContractJobFragment.this);
                        rvAllContract.setAdapter(adapter3);
                        adapter3.notifyDataSetChanged();
                    }
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Contract>> call, Throwable t) {
                Log.d("a", "errr " + t);
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onItemRowClickedd(String contractStatus, String _id) {
        fragmentManager = getFragmentManager();
        ButtonContractDialog buttonContractDialog = new ButtonContractDialog(_id, contractStatus, this);
        buttonContractDialog.show(fragmentManager, TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume", "onResume");
    }

    @Override
    public void onAcceptDialog(String status) {
        if (status.equals("Yes")){

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getContractByJobId();
                }
            },1500);
        }
    }

    @Override
    public void moveFeedback(String flcId, String jobId) {
        fragmentManager = getFragmentManager();
        FragmentGiveFeedback fragmentGiveFeedback = new FragmentGiveFeedback(flcId, jobId);
        fragmentManager.beginTransaction().replace(R.id.rl_list_contract_fragment, fragmentGiveFeedback).addToBackStack(FragmentGiveFeedback.TAG).commit();
    }
}