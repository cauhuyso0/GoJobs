package vn.com.gojobs.Employer;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Adapter.FreelancerAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Model.Follow;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class ListFreelancerNearFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "ListFreelancerNearFragment";
    private ArrayList<Freelancer> freelancers;
    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private LinearLayoutManager linearLayoutManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    private Location currentLocation;
    private String accessTokenDb;
    private CustomProgressBar customProgressBar;

    public ListFreelancerNearFragment(Location location, String accessTokenDb) {
        this.currentLocation = location;
        this.accessTokenDb = accessTokenDb;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        customProgressBar = new CustomProgressBar(getContext());
        View view = inflater.inflate(R.layout.fragment_list_freelancer_near, container, false);

        rlContain = view.findViewById(R.id.rl_job_freelancer_apply);

        rvAllJobQuerryItem = (RecyclerView) view.findViewById(R.id.rvAllFreelancerQuerryItem);
        if (_id != null){

            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else {

            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        getFlcPaginationWithAddress();

        showNoneData();
        return view;
    }

    private String getVillage(LatLng lng) {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        ArrayList<String> arr = new ArrayList<>();
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    lng.latitude, lng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {

                Address address = addressList.get(0);
                if (address.getSubAdminArea() != null) {
                    arr.add(address.getSubAdminArea());
                } else {
                    arr.add("");
                }
            }
        } catch (Exception e) {
        }

        if (arr.size() > 0) {
            return arr.get(0);
        } else {
            return " ";
        }
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

    private void getFlcPaginationWithAddress() {
        final ArrayList<Freelancer> freelancerArrayList = new ArrayList<>();

        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            String name = getVillage(latLng);
            customProgressBar.show();

            API api = retrofitClient.getClien().create(API.class);
            api.getFlcPaginationWithAddress(name + "", GojobConfig.SORT_DESC + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Freelancer>>() {
                @Override
                public void onResponse(Call<List<Freelancer>> call, Response<List<Freelancer>> response) {
                    List<Freelancer> freelancers = response.body();

                    if (freelancers != null) {
                        freelancerArrayList.addAll(freelancers);
                        linearLayoutManager = new LinearLayoutManager(getActivity());
                        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvAllJobQuerryItem.setLayoutManager(layoutManager3);
                        FreelancerAdapter adapter3 = new FreelancerAdapter(getContext(), freelancerArrayList, ListFreelancerNearFragment.this);
                        rvAllJobQuerryItem.setAdapter(adapter3);
                    }
                    customProgressBar.dismiss();
                }

                @Override
                public void onFailure(Call<List<Freelancer>> call, Throwable t) {
                    System.out.println("Error");
                    customProgressBar.dismiss();
                }
            });
        } else {
            Log.d("test", "getPaginationJobWithAddress: chua co location");
        }
    }

    void getFlcByEmpFollow(){
        API api = retrofitClient.getClien().create(API.class);
        api.getFlcByEmpFollow("empId", accessTokenDb).enqueue(new Callback<List<Follow>>() {
            @Override
            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
                System.out.println("code Flc by emp follow: " + response.code());
                List<Follow> follows = response.body();
                System.out.println(follows);
            }

            @Override
            public void onFailure(Call<List<Follow>> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    @Override
    public void onButtonClicked(String nameItem) {
        fragmentManager = getFragmentManager();
        CVFreelancerFragment cvFreelancerFragment = new CVFreelancerFragment(nameItem);
        fragmentManager.beginTransaction().replace(R.id.fragment_list_freelancer, cvFreelancerFragment).addToBackStack(CVFreelancerFragment.TAG).commit();
    }
}