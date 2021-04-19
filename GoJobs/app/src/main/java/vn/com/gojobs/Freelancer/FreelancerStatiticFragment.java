package vn.com.gojobs.Freelancer;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.OTPVerifyFragment;
import vn.com.gojobs.Fragment.TransactionHistoryFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.Model.Wallet;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;

public class FreelancerStatiticFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {

    private PieChart mChart;
    public static final String TAG = "FreelancerStatiticFragment";
    private TextView tvLichSuGiaoDich, tvSoDu, tvRutTien;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId;
    private CustomProgressBar customProgressBar;
    private RatingBar ratingBar;
    private TextView tvTatCaCV, tvCVDaHoanThanh, tvCVDaHuy;
    private static float tatca, daxong, dahuy;
    String accessTokenDb;

    public FreelancerStatiticFragment() {
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
        View view = inflater.inflate(R.layout.fragment_freelancer_statitic, container, false);
        customProgressBar = new CustomProgressBar(getContext());

        if (_id != null) {
            endFlcId = _id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            endFlcId = AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }

        initItem(view);

        getWalletByEndUserId();

        addDataSet(mChart);

        setEventClick();



        return view;
    }

    private void initItem(View view) {
        ratingBar = view.findViewById(R.id.rbFreelancer);
        tvLichSuGiaoDich = view.findViewById(R.id.tv_lich_su_giao_dich);
        tvSoDu = view.findViewById(R.id.tv_so_du);
        tvRutTien = view.findViewById(R.id.tv_rut_tien);
        tvTatCaCV = view.findViewById(R.id.tv_tat_ca_cv);
        tvCVDaHoanThanh = view.findViewById(R.id.tv_cv_da_hoan_thanh);
        tvCVDaHuy = view.findViewById(R.id.tv_cv_da_huy);
        mChart = (PieChart) view.findViewById(R.id.piechart);
        mChart.setRotationEnabled(true);
        mChart.setDescription(new Description());
        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText("PieChart");
        mChart.setCenterTextSize(10);
        mChart.setDrawEntryLabels(true);
        ratingBar.setNumStars(4);
    }

    private void setEventClick() {
        mChart.setOnChartValueSelectedListener(this);
        tvLichSuGiaoDich.setOnClickListener(this);
        tvRutTien.setOnClickListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private static void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float[] yData = {dahuy, daxong, tatca};
        String[] xData = {"January", "February", "January"};

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Thống kê CV");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lich_su_giao_dich:
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                TransactionHistoryFragment transactionHistoryFragment = new TransactionHistoryFragment(endFlcId);
                fragmentTransaction.replace(R.id.rl_freelancer_statitic, transactionHistoryFragment).addToBackStack(TransactionHistoryFragment.TAG).commit();
                break;

            case R.id.tv_rut_tien:
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                OTPVerifyFragment otpVerifyFragment = new OTPVerifyFragment();
                fragmentTransaction.replace(R.id.rl_freelancer_statitic, otpVerifyFragment).addToBackStack(OTPVerifyFragment.TAG).commit();
                break;
        }
    }

    public void getWalletByEndUserId() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);

        // get số dư
        api.getWalletByEndUserId(endFlcId, accessTokenDb).enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                Wallet wallet = response.body();
                if (wallet != null) {
                    tvSoDu.setText(wallet.getBalance() + " đ");
                } else {
                    tvSoDu.setText("0 đ");
                }
            }

            @Override
            public void onFailure(Call<Wallet> call, Throwable t) {
                System.out.println("err");

            }
        });

        //get all công việc
        api.getJobByContractStatus(endFlcId, GojobConfig.STATUS_JOB_ALL + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                Log.d("test", "onResponse: " + response.body());
                Log.d("test", "onResponse: " + endFlcId);

                List<Job> allContracts = response.body();
                if (allContracts.size() != 0) {
                    tatca = allContracts.size();
                } else {
                    tatca = 0;
                }
                tvTatCaCV.setText(tatca + "");
                Log.d("test", "tatca: " + tatca);
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("err");
                tatca = 0;
                tvTatCaCV.setText(tatca + "");
            }
        });

        //get công việc đã xong
        api.getJobByContractStatus(endFlcId, GojobConfig.STATUS_JOB_COMPLETED + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                Log.d("test", "onResponse: " + response.body());
                Log.d("test", "onResponse: " + endFlcId);

                List<Job> allContracts = response.body();
                if (allContracts.size() != 0) {
                    daxong = allContracts.size();
                } else {
                    daxong = 0;
                }
                tvCVDaHoanThanh.setText(daxong + "");
                Log.d("test", "daxong: " + daxong);
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("err");
                daxong = 0;
                tvCVDaHoanThanh.setText(daxong + "");
            }
        });

        //get công việc đã cancel
        api.getJobByContractStatus(endFlcId, GojobConfig.STATUS_JOB_CANCELLED + "", 1, 5, accessTokenDb).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

                Log.d("test", "onResponse: " + response.body());
                Log.d("test", "onResponse: " + endFlcId);

                List<Job> allContracts = response.body();
                if (allContracts.size() != 0) {
                    dahuy = allContracts.size();
                } else {
                    dahuy = 0;
                }
                tvCVDaHuy.setText(dahuy + "");
                customProgressBar.dismiss();
                Log.d("test", "dahuy: " + dahuy);
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                System.out.println("err");
                dahuy = 0;
                tvCVDaHuy.setText(dahuy + "");
                customProgressBar.dismiss();
            }
        });

    }
}