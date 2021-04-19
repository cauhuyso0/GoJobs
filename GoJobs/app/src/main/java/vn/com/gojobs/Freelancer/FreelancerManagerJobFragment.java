package vn.com.gojobs.Freelancer;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import vn.com.gojobs.Adapter.FreelancerManageJobAdapter;
import vn.com.gojobs.R;

public class FreelancerManagerJobFragment extends Fragment {

    public static final String TAG = "FreelancerManagerJobFragment";
    private ViewPager pager;
    private TabLayout tabLayout;

    public FreelancerManagerJobFragment() {
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
        View view = inflater.inflate(R.layout.fragment_freelancer_manager_job, container, false);
        addControl(view);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.bg_color));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
        return view;
    }

    private void addControl(View view) {
        pager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FreelancerManageJobAdapter adapter = new FreelancerManageJobAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }
}