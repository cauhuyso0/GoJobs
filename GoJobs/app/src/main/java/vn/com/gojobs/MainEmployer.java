package vn.com.gojobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.com.gojobs.Employer.MenuEmployerFragment;
import vn.com.gojobs.Employer.MessageEmployerFragment;
import vn.com.gojobs.Employer.NotificationEmployerFragment;
import vn.com.gojobs.Employer.ProfileEmployerFragment;
import vn.com.gojobs.Employer.SearchEmployerFragment;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;
import static vn.com.gojobs.Employer.LoginEmployerFragment.accessTokenDb;

public class MainEmployer extends Fragment {

    BottomNavigationView bnvEmployer;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_employer, null);
        bnvEmployer = view.findViewById(R.id.employer_bnv);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl2,
                new SearchEmployerFragment()).commit();

        bnvEmployer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.employer_navigation_search)
                    fragmentManager.beginTransaction().replace(R.id.fl2,
                            new SearchEmployerFragment()).commit();
                if (item.getItemId() == R.id.employer_navigation_profile)
                    fragmentManager.beginTransaction().replace(R.id.fl2,
                            new ProfileEmployerFragment()).commit();
                if (item.getItemId() == R.id.employer_navigation_notification)
                    fragmentManager.beginTransaction().replace(R.id.fl2,
                            new NotificationEmployerFragment()).commit();
                if (item.getItemId() == R.id.employer_navigation_message)
                    fragmentManager.beginTransaction().replace(R.id.fl2,
                            new MessageEmployerFragment()).commit();
                if (item.getItemId() == R.id.employer_navigation_menu)
                    fragmentManager.beginTransaction().replace(R.id.fl2,
                            new MenuEmployerFragment()).commit();
                return true;
            }
        });

        return view;
    }
}