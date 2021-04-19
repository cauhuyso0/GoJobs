package vn.com.gojobs.Freelancer;

import android.os.Bundle;
import android.util.Log;
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
import vn.com.gojobs.Adapter.MessageAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.ChatFragment;
import vn.com.gojobs.Model.Message;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Freelancer.LoginFreelancerFragment._id;

public class MessageFreelancerFragment extends Fragment implements IItemRowClickedCallback {

    RecyclerView rvMessage_Freelancer;

    private FragmentManager fragmentManager;
    String endFlcId;
    RetrofitClient retrofitClient = new RetrofitClient();
    CustomProgressBar customProgressBar;
    String accessTokenDb;

    public MessageFreelancerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_freelancer_message, null);
        rvMessage_Freelancer = (RecyclerView) view.findViewById(R.id.rvMessage_Freelancer);
        customProgressBar = new CustomProgressBar(getContext());
        fragmentManager = getFragmentManager();
        if (_id != null){
            endFlcId = _id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else {
            endFlcId =  AuthActivity.flcId;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }
        getMessageNotificationByFlc();
      //  getMessageDetail();
        // Message
       // dataDummy();



        return view;
    }

    void getMessageNotificationByFlc(){
        final ArrayList<Message> dsMess = new ArrayList<>();
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getMessageNotificationByFlc(endFlcId, 1, 5, accessTokenDb).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.code() == 200){
                    List<Message> messages = response.body();
                    Log.d("a", "mess " + messages);
                    if (messages != null){
                        dsMess.addAll(messages);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvMessage_Freelancer.setLayoutManager(layoutManager);
                        MessageAdapter adapterMess = new MessageAdapter(getContext(), dsMess, MessageFreelancerFragment.this);
                        rvMessage_Freelancer.setAdapter(adapterMess);
                    }
                }else {
                    Toast.makeText(getContext(), "Bạn chưa có tin nhắn!", Toast.LENGTH_SHORT).show();
                }

                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                System.out.println("errr");
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onButtonClicked(String nameItem) {
        ChatFragment chat = new ChatFragment(nameItem);
        fragmentManager.beginTransaction()
                .replace(R.id.frag_freelancer_message, chat)
                .addToBackStack(chat.TAG).commit();
    }
}
