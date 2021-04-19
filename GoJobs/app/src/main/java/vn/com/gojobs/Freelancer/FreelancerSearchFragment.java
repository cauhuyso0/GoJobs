package vn.com.gojobs.Freelancer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class FreelancerSearchFragment extends Fragment implements IItemRowClickedCallback, View.OnClickListener {

    private RecyclerView rvJobRecommend, rvJobNear, rvAllJobQuerryItem;
    private RelativeLayout llMoveToIndustry;
    private FragmentManager fragmentManager;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private TextView tvMoveToRecommendList, tvMoveToNearJobList, tvMoveToAllJobList;

    private MultiAutoCompleteTextView edtSearch;
    private List<String> nganhNghe = new ArrayList<>();
    private ArrayList<Job> allJobs = new ArrayList<>();
    private List<Job> jobsNear = new ArrayList<>();
    private CustomProgressBar customProgressBar;
    private List<String> list;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    public Location currentLocation;
    private String accessTokenDb;
    private ImageView imgSearch;

    public FreelancerSearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressBar = new CustomProgressBar(getContext());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            focusGPS();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_freelancer_search, null);

        customProgressBar.show();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (LoginFreelancerFragment.accessTokenDb != null) {
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }
        Log.d("a", "token " + accessTokenDb);
        mapItem(view);
        setOnClick();
        setDataSearch();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                getPaginationJobWithFilter();
                if (currentLocation != null) {
                    getPaginationJobWithAddress();
                }
                getPaginationJobWithTime();

            }
        }, 2000);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            focusGPS();
        }

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

    public void focusGPS() {

        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            currentLocation = task.getResult();
                            Log.d("test", "lat : " + currentLocation.getLatitude() + " log : " + currentLocation.getLongitude());
                        }
                    }
                });
    }

    private void mapItem(View view) {
        rvJobRecommend = view.findViewById(R.id.rvRecommendJob);
        rvJobNear = view.findViewById(R.id.rvNearJob);
        rvAllJobQuerryItem = view.findViewById(R.id.rvAllJob);
        llMoveToIndustry = view.findViewById(R.id.llMoveToIndustry);
        edtSearch = view.findViewById(R.id.edt_search_freelancer);
        imgSearch = view.findViewById(R.id.img_search);
        tvMoveToRecommendList = view.findViewById(R.id.tv_see_more_job_recommend_freelancer);
        tvMoveToNearJobList = view.findViewById(R.id.tv_see_more_job_near_freelancer);
        tvMoveToAllJobList = view.findViewById(R.id.tv_see_more_all_job_freelancer);

    }

    private void setOnClick() {
        tvMoveToRecommendList.setOnClickListener(this);
        tvMoveToNearJobList.setOnClickListener(this);
        tvMoveToAllJobList.setOnClickListener(this);
        llMoveToIndustry.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
    }

    private void setDataSearch() {

        API api = retrofitClient.getClien().create(API.class);
        api.getFieldForSearch(accessTokenDb).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                nganhNghe = response.body();
                if (nganhNghe != null){
                    ArrayAdapter adapterLanguages = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, nganhNghe);

                    edtSearch.setAdapter(adapterLanguages);

                    edtSearch.setThreshold(1);

                    // The text separated by commas
                    edtSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    String textSearch = edtSearch.getText().toString();
                    textSearch = textSearch.substring(0,textSearch.length() - 2);

                    fragmentManager = getFragmentManager();
                    FreelancerSearchJobFragment searchJobFragment = new FreelancerSearchJobFragment(textSearch);
                    fragmentManager.beginTransaction().replace(R.id.rl_parent_search_freelancer_fragment, searchJobFragment).addToBackStack(FreelancerSearchJobFragment.TAG).commit();

                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onButtonClicked(String _id) {

        fragmentManager = getFragmentManager();
        JobDetailFragment jobDetailFragment = new JobDetailFragment(_id);
        fragmentManager.beginTransaction().replace(R.id.rl_parent_search_freelancer_fragment, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();

    }

    void getPaginationJobWithTime() {

        API api = retrofitClient.getClien().create(API.class);
        api.getJobWithTime(GojobConfig.SORT_DESC + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                allJobs = (ArrayList<Job>) response.body();

                if (allJobs != null) {
                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvAllJobQuerryItem.setLayoutManager(layoutManager3);
                    JobAdapter adapter3 = new JobAdapter(getContext(), allJobs, FreelancerSearchFragment.this);
                    rvAllJobQuerryItem.setAdapter(adapter3);
                    customProgressBar.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();

            }
        });
    }

    void getPaginationJobWithFilter() {

        list = new ArrayList<>();
        Set<String> set = AuthActivity.sharedPreferences.getStringSet("list_filter", null);
        if (set != null) {
            list.addAll(set);
        }
        API api = retrofitClient.getClien().create(API.class);

        if (list.size() > 0) {
            api.getJobWithFilter(GojobConfig.SORT_DESC + "", list, 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    List<Job> jobsRecommend = response.body();

                    if (jobsRecommend != null) {
                        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        rvJobRecommend.setLayoutManager(layoutManager3);
                        JobAdapter adapter3 = new JobAdapter(getContext(), (ArrayList<Job>) jobsRecommend, FreelancerSearchFragment.this);
                        rvJobRecommend.setAdapter(adapter3);
                    }
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    System.out.println("err");
                }
            });
        } else {
            api.getJobWithTime(GojobConfig.SORT_DESC + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                    allJobs = (ArrayList<Job>) response.body();
                    if (allJobs != null) {
                        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        rvJobRecommend.setLayoutManager(layoutManager3);
                        JobAdapter adapter3 = new JobAdapter(getContext(), allJobs, FreelancerSearchFragment.this);
                        rvJobRecommend.setAdapter(adapter3);
                    }
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    System.out.println("err");
                }
            });
        }
    }

    void getPaginationJobWithAddress() {

        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            String name = getVillage(latLng);
            Log.d("test", "getPaginationJobWithAddress: NAME : " + name);

            API api = retrofitClient.getClien().create(API.class);
            api.getJobWithAddress(GojobConfig.SORT_DESC + "", name + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                    jobsNear = response.body();

                    if (jobsNear != null) {
                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        rvJobNear.setLayoutManager(layoutManager1);
                        JobAdapter adapter1 = new JobAdapter(getContext(), (ArrayList<Job>) jobsNear, FreelancerSearchFragment.this);
                        rvJobNear.setAdapter(adapter1);
                    }
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    System.out.println("err");
                }
            });
        } else {
            Log.d("test", "getPaginationJobWithAddress: chua co location");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_search:
                String textSearch = edtSearch.getText().toString();
                fragmentManager = getFragmentManager();
                FreelancerSearchJobFragment searchJobFragment = new FreelancerSearchJobFragment(textSearch);
                fragmentManager.beginTransaction().replace(R.id.rl_parent_search_freelancer_fragment, searchJobFragment).addToBackStack(FreelancerSearchJobFragment.TAG).commit();
                break;
            case R.id.tv_see_more_job_recommend_freelancer:
                // call api jobs recommend
                fragmentManager = getFragmentManager();
                FreelancerSearchJobRecommendFragment freelancerSearchJobRecommendFragment = new FreelancerSearchJobRecommendFragment(list);
                fragmentManager.beginTransaction().replace(R.id.rl_parent_search_freelancer_fragment, freelancerSearchJobRecommendFragment).addToBackStack(FreelancerSearchJobRecommendFragment.TAG).commit();
                break;
            case R.id.tv_see_more_job_near_freelancer:
                // call api jobs near
                fragmentManager = getFragmentManager();
                FreelancerSearchJobNearFragment freelancerSearchJobNearFragment = new FreelancerSearchJobNearFragment(currentLocation, accessTokenDb);
                fragmentManager.beginTransaction().replace(R.id.rl_parent_search_freelancer_fragment, freelancerSearchJobNearFragment).addToBackStack(FreelancerSearchJobNearFragment.TAG).commit();
                break;
            case R.id.tv_see_more_all_job_freelancer:
                // call api all jobs
                fragmentManager = getFragmentManager();
                FreelancerSearchJobAllFragment freelancerSearchJobAllFragment = new FreelancerSearchJobAllFragment();
                fragmentManager.beginTransaction().replace(R.id.rl_parent_search_freelancer_fragment, freelancerSearchJobAllFragment).addToBackStack(FreelancerSearchJobAllFragment.TAG).commit();
                break;
            case R.id.llMoveToIndustry:
                // move to industry fragment
                fragmentManager = getFragmentManager();
                FreelancerIndustryFragment freelancerIndustryFragment = new FreelancerIndustryFragment();
                fragmentManager.beginTransaction().replace(R.id.main_freelancer, freelancerIndustryFragment).addToBackStack(FreelancerIndustryFragment.TAG).commit();
                break;
        }
    }
}
