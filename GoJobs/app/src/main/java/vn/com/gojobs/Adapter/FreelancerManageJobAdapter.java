package vn.com.gojobs.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vn.com.gojobs.Freelancer.FreelancerJobAppliedFragment;
import vn.com.gojobs.Freelancer.FreelancerJobDoingFragment;
import vn.com.gojobs.Freelancer.FreelancerJobDoneFragment;

public class FreelancerManageJobAdapter extends FragmentStatePagerAdapter {

    public FreelancerManageJobAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new FreelancerJobAppliedFragment();
                break;
            case 1:
                frag = new FreelancerJobDoingFragment();
                break;
            case 2:
                frag = new FreelancerJobDoneFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "CV đã ứng tuyển";
                break;
            case 1:
                title = "CV đang thực hiện";
                break;
            case 2:
                title = "CV đã xong";
                break;
        }
        return title;
    }
}
