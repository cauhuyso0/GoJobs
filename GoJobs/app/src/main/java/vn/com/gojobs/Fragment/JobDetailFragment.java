package vn.com.gojobs.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.DirectionJSONParse;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.Model.RoomMessage;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.dialog.DialogTurnOnGPS;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.ITurnOnGPSCallback;

import static vn.com.gojobs.AuthActivity.flcId;

public class JobDetailFragment extends Fragment implements OnMapReadyCallback, ITurnOnGPSCallback, View.OnClickListener {

    public static final String TAG = "JobDescriptionFragment";

    private LocationManager locationManager;
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout lnBtnSaveBookmark, lnBtnUngTuyen, lnBtnTheoDoi;
    private ImageView imgSaveBookmark, imgUngTuyen;
    private TextView tvSave, tvUngtuyen, tvLienHe, tvTheoDoi, tvTenNhaTuyenDung, tvDiaChi, tvTenCongViec;
    int countSave = 0, countUngTuyen = 0, dem = 0;
    private List<LatLng> listStep = new ArrayList<>();
    ScrollView scrollView;
    String endFlcId, _id, idJobs;
    RetrofitClient retrofitClient = new RetrofitClient();
    private FragmentManager fragmentManager;
    private TextView tvMoTa, tvMucLuong, tvYeuCauKN, tvThoiGianBatDau, tvThoiGianKetThuc, tvHanNopHoSo;
    private CustomProgressBar customProgressBar;
    private String empId, empName, jobTotalSalary;
    String accessTokenDb;
    private ImageView imgEmpLogo;
    private boolean hadFollowed = false;
    private boolean hadSaved = false;

    public JobDetailFragment(String _id) {
        this.idJobs = _id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_job_detail, null);

        customProgressBar = new CustomProgressBar(getContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        imgEmpLogo = view.findViewById(R.id.ivJD_Avatar);
        tvTenNhaTuyenDung = view.findViewById(R.id.tv_ten_nha_tuyen_dung);
        tvDiaChi = view.findViewById(R.id.tv_dia_chi_job);
        tvTenCongViec = view.findViewById(R.id.tv_ten_cong_viec);
        tvMoTa = view.findViewById(R.id.tv_mo_ta_cong_viec);
        tvMucLuong = view.findViewById(R.id.tv_muc_luong);
        tvYeuCauKN = view.findViewById(R.id.tv_yeu_cau_kinh_nghiem_job_detail);
        tvThoiGianBatDau = view.findViewById(R.id.tv_ngay_bat_dau_job_detail);
        tvThoiGianKetThuc = view.findViewById(R.id.tv_ngay_ket_thuc_job_detail);
        tvHanNopHoSo = view.findViewById(R.id.tv_han_nop);

        lnBtnTheoDoi = view.findViewById(R.id.ln_btn_theo_doi);
        tvTheoDoi = view.findViewById(R.id.tv_theo_doi);
        tvLienHe = view.findViewById(R.id.tv_lien_he);
        lnBtnSaveBookmark = view.findViewById(R.id.btnBookmarkJob);
        lnBtnUngTuyen = view.findViewById(R.id.btnApplyJob);
        imgSaveBookmark = view.findViewById(R.id.img_save_bookmark);
        imgUngTuyen = view.findViewById(R.id.img_ungtuyen);
        tvSave = view.findViewById(R.id.tv_save);
        tvUngtuyen = view.findViewById(R.id.tv_ungtuyen);
        scrollView = view.findViewById(R.id.scr_job_description);

        if (LoginFreelancerFragment._id != null) {
            endFlcId = LoginFreelancerFragment._id;
        } else {
            endFlcId = flcId;
        }

        if (endFlcId == null) {
            lnBtnTheoDoi.setVisibility(View.GONE);
            lnBtnSaveBookmark.setVisibility(View.GONE);
            lnBtnUngTuyen.setVisibility(View.GONE);
            tvLienHe.setVisibility(View.GONE);
        }


        if (LoginEmployerFragment.accessTokenDb != null) {
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else if (LoginFreelancerFragment.accessTokenDb != null) {
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else if (AuthActivity.accessTokenDbFlc != null) {
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        } else if (AuthActivity.accessTokenDbEmp != null) {
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        setEvent();
        createMap();
        getJobDetail(idJobs);

        return view;
    }

    private void createMap() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    private void setEvent() {
        lnBtnTheoDoi.setOnClickListener(this);
        tvLienHe.setOnClickListener(this);
        lnBtnUngTuyen.setOnClickListener(this);
        lnBtnSaveBookmark.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBookmarkJob:
                if (hadSaved == false) {
                    createFlcFollowJob();
                    imgSaveBookmark.setBackground(getActivity().getDrawable(R.drawable.ic_bookmark_orange));
                    tvSave.setText("Đã lưu");
                    tvSave.setTextColor(Color.rgb(255, 140, 0));
                    Toast.makeText(getActivity(), "Lưu thành công.", Toast.LENGTH_SHORT).show();
                    hadSaved = true;
                } else {
                    createFlcFollowJob();
                    imgSaveBookmark.setBackground(getActivity().getDrawable(R.drawable.ic_bookmark_black));
                    tvSave.setText("Lưu lại");
                    tvSave.setTextColor(Color.rgb(0, 0, 0));
                    Toast.makeText(getActivity(), "Hủy lưu thành công.", Toast.LENGTH_SHORT).show();
                    hadSaved = false;
                }
                break;

            case R.id.btnApplyJob:
                if (countUngTuyen % 2 == 0) {
                    countUngTuyen++;
                    createContract();
                } else {
                    imgUngTuyen.setBackground(getActivity().getDrawable(R.drawable.ic_check_circle_black));
                    tvUngtuyen.setText("Ứng tuyển");
                    tvUngtuyen.setTextColor(Color.rgb(0, 0, 0));
                    countUngTuyen++;
                }
                break;

            case R.id.ln_btn_theo_doi:
                if (hadFollowed == false) {
                    createFlcFollowEmp();
                    tvTheoDoi.setText("Đã theo dõi");
                    Toast.makeText(getActivity(), "Theo dõi thành công.", Toast.LENGTH_SHORT).show();
                    hadFollowed = true;
                } else {
                    createFlcFollowEmp();
                    tvTheoDoi.setText("Theo dõi");
                    Toast.makeText(getActivity(), "Hủy theo dõi thành công.", Toast.LENGTH_SHORT).show();
                    hadFollowed = false;
                }
                break;

            case R.id.tv_lien_he:
                newChat();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        FragmentManager fm = getChildFragmentManager();
        GojobSupportMapFragment gojobSupportMapFragment = (GojobSupportMapFragment) fm.findFragmentByTag("map_fragment");
        if (gojobSupportMapFragment == null) {
            gojobSupportMapFragment = new GojobSupportMapFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.map, gojobSupportMapFragment, "map_fragment");
            fragmentTransaction.commit();
            fm.executePendingTransactions();
        }
        gojobSupportMapFragment.getMapAsync(this);
        gojobSupportMapFragment.setListener(new GojobSupportMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    @Override
    public void turnOnGPS() {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void showDialogGPS() {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DialogTurnOnGPS dialogTurnOnGPS = new DialogTurnOnGPS(this);
        dialogTurnOnGPS.show(fragmentTransaction, DialogTurnOnGPS.TAG);
    }

    private void getJobLocation(String location) {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((latLng), 17));
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    private void checkFollow() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        Log.d("s", "id :" + empId + " " + endFlcId);
        api.hasFollowJob(empId, endFlcId, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("a", "code follow: " + response.code());
                if (response.code() == 200) {
                    hadFollowed = true;
                    tvTheoDoi.setText("Đã theo dõi");
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("b", "errr: " + t);
                customProgressBar.dismiss();
            }
        });
    }

    private void checkSaved() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.hadSaved(idJobs, endFlcId,accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("a", "code follow job: " + response.code());
                if (response.code() == 200) {
                    hadSaved = true;
                    imgSaveBookmark.setBackground(getActivity().getDrawable(R.drawable.ic_bookmark_orange));
                    tvSave.setText("Đã lưu");
                    tvSave.setTextColor(Color.rgb(255, 140, 0));
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("b", "errr: " + t);
                customProgressBar.dismiss();
            }
        });
    }

    public String makeURL(String sourceLat, String sourceLng, String destLat, String destLng) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(sourceLat);
        urlString.append(",");
        urlString.append(sourceLng);
        urlString.append("&amp;");
        urlString.append("destination=");
        urlString.append(destLat);
        urlString.append(",");
        urlString.append(destLng);
        urlString.append("&amp;");
        urlString.append("key=" + getActivity().getString(R.string.google_api_key));

        return urlString.toString();
    }

    final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            String request = makeURL("10.762643", "106.682079", "10.774467", "106.703274");
            DirectionJSONParse directionJSONParse = new DirectionJSONParse(request);
            ArrayList<LatLng> list = directionJSONParse.testDirection();
            for (LatLng latLng : list) {
                listStep.add(latLng);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMap.addPolyline(new PolylineOptions()
                    .addAll(listStep)
                    .width(6)
                    .color(Color.rgb(86, 151, 255))
            );
        }
    };

    // freelancer theo doi employer
    void createFlcFollowEmp() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.createFlcFollowEmp(endFlcId + "", empId + "", endFlcId + "", accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }

    //freelancer huy theo doi employer
    void delFollowEmpByFlc() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.delFollowEmpByFlc(empId + "", endFlcId + "", empName + "", accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code del follow job " + response.code());

                if (response.code() == 200) {
                    tvTheoDoi.setText("Theo dõi");
                } else {
                    Toast.makeText(getActivity(), "Theo dõi thất bại.", Toast.LENGTH_SHORT).show();
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Err");
                customProgressBar.dismiss();
                Toast.makeText(getActivity(), "Hủy theo dõi thất bại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //freelancer luu cv
    void createFlcFollowJob() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.createFlcFollowJob(endFlcId + "", idJobs + "", "" + endFlcId, accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code create flc follow job: " + response.code());
                customProgressBar.dismiss();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });
    }

    //freelacer huy luu cv
    void delFollowJobByFlc() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.delFollowJobByFlc(idJobs + "", endFlcId + "", accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code del follow job: " + response.code());

                imgSaveBookmark.setBackground(getActivity().getDrawable(R.drawable.ic_bookmark_black));
                tvSave.setText("Lưu lại");
                tvSave.setTextColor(Color.rgb(0, 0, 0));
                customProgressBar.dismiss();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });
    }

    void createContract() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.createContractByFreelancer(idJobs + "", endFlcId + "",
                jobTotalSalary + "",
                GojobConfig.STATUS_JOB_APPLIED + "",
                empId + "", accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                imgUngTuyen.setBackground(getActivity().getDrawable(R.drawable.ic_check_circle_orange));
                tvUngtuyen.setText("Đã ứng tuyển");
                tvUngtuyen.setTextColor(Color.rgb(255, 140, 0));
                customProgressBar.dismiss();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Ứng tuyển thất bại.", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
            }
        });
    }

    //detail job
    void getJobDetail(String idJobs) {

        Log.d("test", "callJobDetail: id job detail fragment : " + idJobs);
        customProgressBar.show();

        API api = retrofitClient.getClien().create(API.class);
        api.getJobDetail(idJobs, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                List<Job> jobs = response.body();

                if (jobs.size() != 0) {
                    Job job = jobs.get(0);

                    if (job.getEmpId().getEmpLogo() != null) {
                        String avatar = job.getEmpId().getEmpLogo();
                        // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                        byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        imgEmpLogo.setImageBitmap(decodedByte);
                    } else {
                        imgEmpLogo.setImageDrawable(getActivity().getDrawable(R.drawable.logo_splash));
                    }

                    empId = job.getEmpId().get_id();
                    empName = job.getEmpId().getEmpName();
                    jobTotalSalary = job.getJobTotalSalaryPerHeadCount() + "";
                    tvTenNhaTuyenDung.setText(job.getEmpId().getEmpName());
                    tvDiaChi.setText(job.getJobAddress());

                    getJobLocation(job.getJobAddress());

                    tvTenCongViec.setText(job.getJobTitle());
                    tvMoTa.setText(job.getJobDescription());
                    tvMucLuong.setText(job.getJobSalary() + "");
                    if (job.isExperiencRequired()) {
                        tvYeuCauKN.setText("Có");
                    } else {
                        tvYeuCauKN.setText("Không");
                    }
                    tvThoiGianBatDau.setText(job.getJobStart());
                    tvThoiGianKetThuc.setText(job.getJobEnd());
                    tvHanNopHoSo.setText(job.getJobEnd());

                    checkFollow();
                    checkSaved();
                }

                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Log.d("test", "onFailure: fail to get job detail");
                customProgressBar.dismiss();
            }
        });
    }

    private void newChat() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.newMessage(empId, endFlcId, accessTokenDb).enqueue(new Callback<RoomMessage>() {
            @Override
            public void onResponse(Call<RoomMessage> call, Response<RoomMessage> response) {
                RoomMessage message = response.body();
                _id = message.get_id();
                if (_id != null) {
                    fragmentManager = getFragmentManager();
                    ChatFragment chatFragment = new ChatFragment(_id);
                    fragmentManager.beginTransaction().replace(R.id.frag_job_detail, chatFragment).addToBackStack(ChatFragment.TAG).commit();
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<RoomMessage> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }
}
