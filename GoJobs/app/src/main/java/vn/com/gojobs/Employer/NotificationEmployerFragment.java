package vn.com.gojobs.Employer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Adapter.NotificationAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Model.Notification;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.ItemRowClickedCallbackForNotification;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class NotificationEmployerFragment extends Fragment implements ItemRowClickedCallbackForNotification {

    RecyclerView rvNotification_Employer;
    private FragmentManager fragmentManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endEmpId;
    String accessTokenDb;

    public NotificationEmployerFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_employer_notification,null);
        rvNotification_Employer = (RecyclerView) view.findViewById(R.id.rvNotification_Employer);

        // Message

        if (_id != null){
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        getNotificationByEmp();
        return view;

    }

    @Override
    public void onItemRowClicked(String _id, String content, String empId) {

            fragmentManager = getFragmentManager();
            ListContractJobFragment listContractJobFragment = new ListContractJobFragment(_id, "APPLIED");
            fragmentManager.beginTransaction().replace(R.id.fl2, listContractJobFragment).addToBackStack(ListContractJobFragment.TAG).commit();

    }
    void getNotificationByEmp(){
        final ArrayList<Notification> notificationArrayList = new ArrayList<>();
        API api = retrofitClient.getClien().create(API.class);
        api.getNotificationForEmp(endEmpId,1, 5, accessTokenDb).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.code() == 200){
                    List<Notification> notifications = response.body();

                        notificationArrayList.addAll(notifications);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvNotification_Employer.setLayoutManager(layoutManager);
                        NotificationAdapter adapter = new NotificationAdapter(getContext(), notificationArrayList, NotificationEmployerFragment.this);

                        rvNotification_Employer.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getContext(), "Bạn chưa có thông báo!", Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                System.out.println("cc" + t);
            }
        });
    }


}
