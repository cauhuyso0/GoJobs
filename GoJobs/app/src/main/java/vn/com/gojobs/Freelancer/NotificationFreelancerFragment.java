package vn.com.gojobs.Freelancer;

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
import vn.com.gojobs.Fragment.FragmentGiveFeedback;
import vn.com.gojobs.Fragment.JobDetailFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.Notification;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.ItemRowClickedCallbackForNotification;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;

public class NotificationFreelancerFragment extends Fragment implements ItemRowClickedCallbackForNotification {

    RecyclerView rvNotification_Freelancer;

    private FragmentManager fragmentManager;
    String endFlcId;
    RetrofitClient retrofitClient = new RetrofitClient();
    String accessTokenDb;


    public NotificationFreelancerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_freelancer_notification,null);

        rvNotification_Freelancer = (RecyclerView) view.findViewById(R.id.rvNotification_Freelancer);
        if (_id != null){
            endFlcId = _id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else {
            endFlcId = AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }
        getNotification();
     //   getJobDetail();



        return view;

    }

    @Override
    public void onItemRowClicked(String jobId, String content, String empId) {
        if (content.equals(GojobConfig.CONTENT_NOTIFICATION_CREATED_JOB)){
            fragmentManager = getFragmentManager();
            JobDetailFragment jobDetailFragment = new JobDetailFragment(jobId);
            fragmentManager.beginTransaction().replace(R.id.fl, jobDetailFragment).addToBackStack(JobDetailFragment.TAG).commit();
        }else if (content.equals(GojobConfig.CONTENT_NOTIFICATION_COMPLETED)){
            fragmentManager = getFragmentManager();
            FragmentGiveFeedback fragmentGiveFeedback = new FragmentGiveFeedback(empId,jobId);
            fragmentManager.beginTransaction().replace(R.id.fl, fragmentGiveFeedback).addToBackStack(FragmentGiveFeedback.TAG).commit();
        }else if (content.equals(GojobConfig.CONTENT_NOTIFICATION_CANCELED_JOB)){
            Toast.makeText(getContext(), "Công việc đã bị xóa", Toast.LENGTH_SHORT).show();
        }

    }

   void getNotification (){
       final ArrayList<Notification> dsNoti =new ArrayList<>();
       API api = retrofitClient.getClien().create(API.class);
       api.getNotificationFlc(endFlcId, 1 , 5, accessTokenDb).enqueue(new Callback<List<Notification>>() {
           @Override
           public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.code() == 200){
                    List<Notification> notifications = response.body();
                    dsNoti.addAll(notifications);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvNotification_Freelancer.setLayoutManager(layoutManager);
                        NotificationAdapter adapterNoti = new NotificationAdapter(getContext(), dsNoti, NotificationFreelancerFragment.this);
                        rvNotification_Freelancer.setAdapter(adapterNoti);
                }else {
                    Toast.makeText(getContext(), "Bạn chưa có thông báo!", Toast.LENGTH_SHORT).show();
                }


           }

           @Override
           public void onFailure(Call<List<Notification>> call, Throwable t) {
               System.out.println("eerrr " + t);
           }
       });
   }
//   public void getJobDetail(){
//        API api = retrofitClient.getClien().create(API.class);
//        api.getJobDetail().enqueue(new Callback<List<Job>>() {
//            @Override
//            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Job>> call, Throwable t) {
//
//            }
//        });
//   }
}
