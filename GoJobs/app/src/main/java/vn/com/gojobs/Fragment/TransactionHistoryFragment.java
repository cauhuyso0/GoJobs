package vn.com.gojobs.Fragment;

import android.os.Bundle;
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
import vn.com.gojobs.Adapter.HistoryTransactionAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.Model.Receipt;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

public class TransactionHistoryFragment extends Fragment {

    public static final String TAG = "TransactionHistoryFragment";
    RetrofitClient retrofitClient = new RetrofitClient();
    private ArrayList<Receipt> receipts;

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    private String _id;
    RecyclerView rc_transaction_history;
   // LinearLayoutManager linearLayoutManager;
    String endEmpId;
    String accessTokenDb;
    private CustomProgressBar customProgressBar;


    public TransactionHistoryFragment(String id) {
        this._id = id;
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
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        customProgressBar = new CustomProgressBar(getContext());
        rc_transaction_history = view.findViewById(R.id.rc_transaction_history);

        if (LoginEmployerFragment._id != null) {
            endEmpId = LoginEmployerFragment._id;
        } else {
            endEmpId = AuthActivity.empId;
        }
        if (LoginEmployerFragment.accessTokenDb != null){
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else if (LoginFreelancerFragment.accessTokenDb != null){
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else if (AuthActivity.accessTokenDbFlc != null){
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }else if (AuthActivity.accessTokenDbEmp != null){
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        
        getReceiptHistory();

        rlContain = view.findViewById(R.id.rl_history_transaction_fragment);
        rvAllJobQuerryItem = view.findViewById(R.id.rc_transaction_history);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        getReceiptHistory();

        return view;
    }

    private void getReceiptHistory(){

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getReceiptHistory(_id, 1, 5, accessTokenDb).enqueue(new Callback<List<Receipt>>() {
            @Override
            public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {

                receipts = (ArrayList<Receipt>) response.body();

                if (receipts != null && receipts.size() != 0) {
                    rvAllJobQuerryItem.setLayoutManager(linearLayoutManager);
                    HistoryTransactionAdapter adapter3 = new HistoryTransactionAdapter(getContext(), receipts);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                    System.out.println(receipts);
                } else {
                    Toast.makeText(getActivity(), "Bạn chưa có giao dịch nào.", Toast.LENGTH_SHORT).show();
                }
                customProgressBar.dismiss();
             }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });
    }
}