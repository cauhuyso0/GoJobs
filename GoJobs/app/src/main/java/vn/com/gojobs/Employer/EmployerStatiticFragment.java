package vn.com.gojobs.Employer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.RechargeFragment;
import vn.com.gojobs.Fragment.TransactionHistoryFragment;
import vn.com.gojobs.R;

public class EmployerStatiticFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {

    private PieChart mChart;
    public static final String TAG = "EmployerStatiticFragment";
    private TextView tvLichSuGiaoDich, tvNapTien;
    private FragmentManager fragmentManager;
    private String endEmpId;

    public EmployerStatiticFragment() {
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
        View view = inflater.inflate(R.layout.fragment_employer_statitic, container, false);
        if (LoginEmployerFragment._id != null){
            endEmpId = LoginEmployerFragment._id;
        }else {
            endEmpId =  AuthActivity.empId;
        }
        tvLichSuGiaoDich = view.findViewById(R.id.tv_lich_su_giao_dich);
        tvNapTien = view.findViewById(R.id.tv_rut_tien);

        mChart = (PieChart) view.findViewById(R.id.piechart);
        mChart.setRotationEnabled(true);
        mChart.setDescription(new Description());
        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText("PieChart");
        mChart.setCenterTextSize(10);
        mChart.setDrawEntryLabels(true);

        addDataSet(mChart);

        mChart.setOnChartValueSelectedListener(this);
        tvNapTien.setOnClickListener(this);
        tvLichSuGiaoDich.setOnClickListener(this);

        return view;
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
        float[] yData = { 25, 40, 70 };
        String[] xData = { "January", "February", "January" };

        for (int i = 0; i < yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],i));
        }
        for (int i = 0; i < xData.length;i++){
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet=new PieDataSet(yEntrys,"Biểu đồ");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);

        pieDataSet.setColors(colors);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        fragmentManager = getFragmentManager();
        switch (view.getId()){
            case R.id.tv_lich_su_giao_dich:
                fragmentManager = getFragmentManager();

                TransactionHistoryFragment transactionHistoryFragment = new TransactionHistoryFragment(endEmpId);
                fragmentManager.beginTransaction().replace(R.id.rl_employer_statitic, transactionHistoryFragment).addToBackStack(TransactionHistoryFragment.TAG).commit();
                break;

            case R.id.tv_rut_tien:

                fragmentManager = getFragmentManager();
                RechargeFragment rechargeFragment = new RechargeFragment();
                fragmentManager.beginTransaction().replace(R.id.rl_employer_statitic, rechargeFragment).addToBackStack(RechargeFragment.TAG).commit();
                break;
        }
    }
}