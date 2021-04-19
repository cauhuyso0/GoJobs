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
import vn.com.gojobs.Adapter.MessageAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.ChatFragment;
import vn.com.gojobs.Model.Message;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class MessageEmployerFragment extends Fragment implements IItemRowClickedCallback {

    RecyclerView rvMessage_Employer;
    ArrayList<Message> dsMess =new ArrayList<>();
    private FragmentManager fragmentManager;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endEmpId;
    CustomProgressBar customProgressBar;
    String accessTokenDb;

    public MessageEmployerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_employer_message,null);

        rvMessage_Employer = (RecyclerView) view.findViewById(R.id.rvMessage_Employer);
        fragmentManager = getFragmentManager();
        if (_id != null){
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }
        customProgressBar = new CustomProgressBar(getContext());
        getMessageNotificationByEmp();



        return view;
    }


    void getMessageNotificationByEmp(){
        customProgressBar.show();
        final ArrayList<Message> messageArrayList = new ArrayList<>();
        API api = retrofitClient.getClien().create(API.class);
        api.getMessageNotificationByEmp(endEmpId, 1, 5, accessTokenDb).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.code() == 200){
                    List<Message> messages = response.body();
                    messageArrayList.addAll(messages);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvMessage_Employer.setLayoutManager(layoutManager);
                    MessageAdapter adapterMess = new MessageAdapter(getContext(), messageArrayList,MessageEmployerFragment.this);
                    rvMessage_Employer.setAdapter(adapterMess);
                    System.out.println(messageArrayList + "sss");
                }else {
                    Toast.makeText(getContext(), "Bạn chưa có tin nhắn!", Toast.LENGTH_SHORT).show();
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                System.out.println("err " + t);
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onButtonClicked(String nameItem) {
        ChatFragment chat = new ChatFragment(nameItem);
        fragmentManager.beginTransaction()
                .replace(R.id.frg_employer_mesage, chat)
                .addToBackStack(chat.TAG).commit();
    }
}
