package vn.com.gojobs.Freelancer;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

public class FreelancerSearchJobNearFragment extends Fragment implements IItemRowClickedCallback {

    public static final String TAG = "FreelancerSearchJobNearFragment";

    FragmentManager fragmentManager;
    private RecyclerView rvAllJobQuerryItem;
    private RelativeLayout rlContain;
    private Location currentLocation;
    private String accessTokenDb;
    RetrofitClient retrofitClient = new RetrofitClient();
    ArrayList<Job> jobsNear = new ArrayList<>();
    private CustomProgressBar customProgressBar;

    public FreelancerSearchJobNearFragment(Location location, String accessTokenDb) {
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
        View view = inflater.inflate(R.layout.freelancer_search_job_near_fragment, container, false);

        customProgressBar = new CustomProgressBar(getContext());
        rlContain = view.findViewById(R.id.rl_freelancer_search_job_near);
        rvAllJobQuerryItem = view.findViewById(R.id.rvAllFlcQuerryItem);

        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            String name = getVillage(latLng);
            Log.d("test", "getPaginationJobWithAddress: NAME : " + name);

            customProgressBar.show();
            API api = retrofitClient.getClien().create(API.class);
            api.getJobWithAddress(GojobConfig.SORT_DESC + "", name + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                    jobsNear = (ArrayList<Job>) response.body();

                    if (jobsNear != null) {
                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        rvAllJobQuerryItem.setLayoutManager(layoutManager1);
                        JobAdapter adapter1 = new JobAdapter(getContext(), (ArrayList<Job>) jobsNear, FreelancerSearchJobNearFragment.this);
                        rvAllJobQuerryItem.setAdapter(adapter1);
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
            Log.d("test", "getPaginationJobWithAddress: chua co location");
        }

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

    @Override
    public void onButtonClicked(String _id) {

        fragmentManager = getActivity().getSupportFragmentManager();
        JobDetailFragment jobDetailFragment = new JobDetailFragment(_id);
        fragmentManager.beginTransaction().replace(R.id.frag_freelancer_manager_job, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();

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