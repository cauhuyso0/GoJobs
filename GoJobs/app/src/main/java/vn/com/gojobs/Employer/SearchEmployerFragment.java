package vn.com.gojobs.Employer;

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

public class SearchEmployerFragment extends Fragment implements IItemRowClickedCallback, View.OnClickListener {

    RecyclerView rvRecommendResume, rvNearResume;
    ImageView img_search;
    MultiAutoCompleteTextView edtSearch;
    String accessTokenDb;
    private CustomProgressBar customProgressBar;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    public Location currentLocation;

    private List<String> nganhNghe= new ArrayList<>();
//    [] nganhNghe = {"Kĩ sư phần mền", "Thiết kế đồ họa", "Lao động phổ thông", "Phụ bếp, nhà hàng - khách sạn",
//            "Hướng dẫn viên du lịch", "Tiếp thị", "Nhân viên BĐS",
//            "Giúp việc", "Giữ trẻ", "Chăm sóc thú cưng", "Dạy kèm", "Phụ quán"};

    public static ArrayList<Freelancer> dsFreelancerSearch = new ArrayList<>();

    RetrofitClient retrofitClient = new RetrofitClient();
    String endEmpId;
    private FragmentManager fragmentManager;
    private TextView tvMoreFreelancerRecommend, tvMoreFreelancerNear;
    private ArrayList<Freelancer> freelancerArrayList = new ArrayList<>();;

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
        final View view = inflater.inflate(R.layout.fragment_employer_search, null);
        customProgressBar = new CustomProgressBar(getContext());
        // ánh xạ và set acction click
        mapItem(view);

        //dummyData();
        setDataSearch();
        if (_id != null) {
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        customProgressBar.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFlcPaginationWithRecommend();

                if (currentLocation != null) {

                    getFlcPaginationWithAddress();
                }

                customProgressBar.dismiss();
            }
        }, 2000);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            focusGPS();
        }
        return view;

    }

    private void mapItem(View view) {
        rvRecommendResume = (RecyclerView) view.findViewById(R.id.rvRecommendResume);
        rvNearResume = (RecyclerView) view.findViewById(R.id.rvNearResume);
        edtSearch = view.findViewById(R.id.edt_search_employer);
        tvMoreFreelancerRecommend = view.findViewById(R.id.tv_see_more_freelancer_recommend_employer);
        tvMoreFreelancerNear = view.findViewById(R.id.tv_see_more_freelancer_near_employer);
        img_search = view.findViewById(R.id.img_search);
        img_search.setOnClickListener(this);
        tvMoreFreelancerRecommend.setOnClickListener(this);
        tvMoreFreelancerNear.setOnClickListener(this);
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

                    fragmentManager = getFragmentManager();
                    ListFreelancerRecommendFragment listFreelancerRecommend1 = new ListFreelancerRecommendFragment(textSearch);
                    fragmentManager.beginTransaction().replace(R.id.rl_parent_search_employer, listFreelancerRecommend1).addToBackStack(ListFreelancerRecommendFragment.TAG).commit();

                    handled = true;
                }
                return handled;
            }
        });

    }

    private void getFlcPaginationWithRecommend() {

        API api = retrofitClient.getClien().create(API.class);
        api.getFlcPaginationAll(GojobConfig.SORT_DESC + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Freelancer>>() {
            @Override
            public void onResponse(Call<List<Freelancer>> call, Response<List<Freelancer>> response) {

                List<Freelancer> freelancers = response.body();
                if (freelancers != null) {
                    freelancerArrayList.addAll(freelancers);
                    dsFreelancerSearch = freelancerArrayList;
                    LinearLayoutManager layoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rvRecommendResume.setLayoutManager(layoutManager4);
                    FreelancerAdapter adapter4 = new FreelancerAdapter(getContext(), freelancerArrayList, SearchEmployerFragment.this);

                    rvRecommendResume.setAdapter(adapter4);
                    adapter4.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Freelancer>> call, Throwable t) {
            }
        });
    }

    private void getFlcPaginationWithAddress() {

        final ArrayList<Freelancer> freelancerArrayList = new ArrayList<>();

        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            String name = getVillage(latLng);
            Log.d("test", "getPaginationJobWithAddress: NAME : " + name);

            API api = retrofitClient.getClien().create(API.class);
            api.getFlcPaginationWithAddress(name + "", GojobConfig.SORT_DESC, 1, 5, accessTokenDb).enqueue(new Callback<List<Freelancer>>() {
                @Override
                public void onResponse(Call<List<Freelancer>> call, Response<List<Freelancer>> response) {
                    List<Freelancer> freelancers = response.body();

                    if (freelancers != null) {
                        freelancerArrayList.addAll(freelancers);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvNearResume.setLayoutManager(layoutManager);
                        FreelancerAdapter adapter = new FreelancerAdapter(getContext(), freelancerArrayList, SearchEmployerFragment.this);

                        rvNearResume.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<List<Freelancer>> call, Throwable t) {
                    System.out.println("Error");
                }
            });
        } else {
            Log.d("test", "getPaginationJobWithAddress: chua co location");
        }

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
    public void onButtonClicked(String nameItem) {
        //freelancerDetail(nameItem);
        fragmentManager = getFragmentManager();
        CVFreelancerFragment cvFreelancerFragment = new CVFreelancerFragment(nameItem);
        fragmentManager.beginTransaction().replace(R.id.rl_parent_search_employer, cvFreelancerFragment).addToBackStack(CVFreelancerFragment.TAG).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_see_more_freelancer_recommend_employer:

                fragmentManager = getFragmentManager();
                ListFreelancerRecommendFragment listFreelancerRecommend = new ListFreelancerRecommendFragment("");
                fragmentManager.beginTransaction().replace(R.id.rl_parent_search_employer, listFreelancerRecommend).addToBackStack(ListFreelancerRecommendFragment.TAG).commit();
                break;

            case R.id.tv_see_more_freelancer_near_employer:
                fragmentManager = getFragmentManager();
                ListFreelancerNearFragment listFreelancerNear = new ListFreelancerNearFragment(currentLocation, accessTokenDb);
                fragmentManager.beginTransaction().replace(R.id.rl_parent_search_employer, listFreelancerNear).addToBackStack(ListFreelancerNearFragment.TAG).commit();
                break;

            case R.id.img_search:

                if (!edtSearch.getText().toString().equals("")) {
                    dsFreelancerSearch.clear();
                    fragmentManager = getFragmentManager();
                    ListFreelancerRecommendFragment listFreelancerRecommend1 = new ListFreelancerRecommendFragment(edtSearch.getText().toString());
                    fragmentManager.beginTransaction().replace(R.id.rl_parent_search_employer, listFreelancerRecommend1).addToBackStack(ListFreelancerRecommendFragment.TAG).commit();
                }
                break;
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
}
