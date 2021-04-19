package vn.com.gojobs.Freelancer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import vn.com.gojobs.Adapter.IndustryAdapter;
import vn.com.gojobs.Model.Industry;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static android.content.Context.MODE_PRIVATE;

public class FreelancerIndustryFragment extends Fragment implements IItemRowClickedCallback {

    GridView gvIndustry;
    Button btnIndustryBack, btnIndustrySubmit;
    public final static String TAG = "IndustryFragment";
    public static ArrayList<String> listFilter = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private final String KEY_LIST_FILTER_JOB_FLC = "flcEmail";
    private final String SHARE_PREF_NAME = "mypref";

    public FreelancerIndustryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_industry,null);

        sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        Set<String> set = sharedPreferences.getStringSet("list_filter", null);
        if (set != null) {
            listFilter.clear();
            listFilter.addAll(set);
            Log.d("test", "get list: " + set.toString());
        }

        // ánh xạ
        btnIndustryBack = view.findViewById(R.id.btnIndustryBack);
        btnIndustrySubmit = view.findViewById(R.id.btnIndustrySubmit);
        gvIndustry = (GridView) view.findViewById(R.id.gvIndistry);

        ArrayList<Industry> industryArrayList = new ArrayList<Industry>();
        industryArrayList.add(new Industry("Thương mại"));
        industryArrayList.add(new Industry("Bán hàng"));
        industryArrayList.add(new Industry("Tư vấn"));
        industryArrayList.add(new Industry("Điện lạnh"));
        industryArrayList.add(new Industry("Điện tử"));
        industryArrayList.add(new Industry("Cơ điện"));
        industryArrayList.add(new Industry("Kho vận"));
        industryArrayList.add(new Industry("Vận tải"));
        industryArrayList.add(new Industry("Lao động phổ thông"));
        industryArrayList.add(new Industry("Nông nghiệp"));
        industryArrayList.add(new Industry("Xây dựng"));
        industryArrayList.add(new Industry("Sản xuất"));
        industryArrayList.add(new Industry("Nhà hàng"));
        industryArrayList.add(new Industry("Khách sạn"));
        industryArrayList.add(new Industry("Giáo dục"));
        industryArrayList.add(new Industry("Du lịch"));

        IndustryAdapter adapter = new IndustryAdapter(getContext(), industryArrayList, this);
        gvIndustry.setAdapter(adapter);

        btnIndustryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnIndustrySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Set the values
                Set<String> set = new HashSet<>();
                if (listFilter.size() > 0) {
                    sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    set.addAll(listFilter);
                    editor.putStringSet("list_filter", set);
                    editor.commit();
                    editor.apply();

                    Log.d("test", "submit list: " + set.toString());
                    listFilter.clear();
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;

    }

    @Override
    public void onButtonClicked(String nameItem) {

    }
}
