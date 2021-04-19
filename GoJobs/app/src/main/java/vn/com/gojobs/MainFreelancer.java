package vn.com.gojobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.com.gojobs.Freelancer.MenuFreelancerFragment;
import vn.com.gojobs.Freelancer.MessageFreelancerFragment;
import vn.com.gojobs.Freelancer.NotificationFreelancerFragment;
import vn.com.gojobs.Freelancer.ProfileFreelancerFragment;
import vn.com.gojobs.Freelancer.FreelancerSearchFragment;

public class MainFreelancer extends Fragment {

    BottomNavigationView bnvFreelancer;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_freelancer, null);
        bnvFreelancer = view.findViewById(R.id.freelancer_bnv);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl,
                new FreelancerSearchFragment()).commit();

        bnvFreelancer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.freelancer_navigation_search)

                    fragmentManager.beginTransaction().replace(R.id.fl,
                            new FreelancerSearchFragment()).commit();

                if (item.getItemId() == R.id.freelancer_navigation_profile)

                    fragmentManager.beginTransaction().replace(R.id.fl,
                            new ProfileFreelancerFragment()).commit();

                if (item.getItemId() == R.id.freelancer_navigation_notification)

                    fragmentManager.beginTransaction().replace(R.id.fl,
                            new NotificationFreelancerFragment()).commit();

                if (item.getItemId() == R.id.freelancer_navigation_message)

                    fragmentManager.beginTransaction().replace(R.id.fl,
                            new MessageFreelancerFragment()).commit();

                if (item.getItemId() == R.id.freelancer_navigation_menu)

                    fragmentManager.beginTransaction().replace(R.id.fl,
                            new MenuFreelancerFragment()).commit();

                return true;
            }
        });

        return view;
    }
}
