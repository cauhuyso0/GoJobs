package vn.com.gojobs.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Adapter.ChatAdapter;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.Model.MessageFormat;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

public class ChatFragment extends Fragment {


    private EditText textField;
    private ImageButton sendButton;

    public static final String TAG  = "ChatFragment";
    public static String uniqueId;
    private String _id;
    public static String endEmpId;
    public static String endFlcId;
    private String Username, name;
    String emp, flc;
    CustomProgressBar customProgressBar;

    private Boolean hasConnection = false;
    RetrofitClient retrofitClient = new RetrofitClient();

    private ListView messageListView;
    private ChatAdapter chatAdapter;
    String empId;
    String user;
    String idFlc;
    String roomId;
    int i;
    private Thread thread2;
    private boolean startTyping = false;
    private int time = 2;
    private JSONArray formatList = new JSONArray();
    String accessTokenDb;

    public ChatFragment(String _id) {
        this._id = _id;
    }


    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(GojobConfig.ROOT_URL);
        } catch (URISyntaxException e) {}
    }

    @SuppressLint("HandlerLeak")
    Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: typing stopped " + startTyping);
            if(time == 0){
                getActivity().setTitle("SocketIO");
                Log.i(TAG, "handleMessage: typing stopped time is " + time);
                startTyping = false;
                time = 2;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        customProgressBar = new CustomProgressBar(getContext());

        if (LoginEmployerFragment.accessTokenDb != null){
            endEmpId = LoginEmployerFragment._id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        }else if (LoginFreelancerFragment.accessTokenDb != null){

            endFlcId = LoginFreelancerFragment._id;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        }else if (AuthActivity.accessTokenDbFlc != null){
            accessTokenDb = AuthActivity.accessTokenDbFlc;
            endFlcId = AuthActivity.flcId;
        }else if (AuthActivity.accessTokenDbEmp != null){
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        System.out.println(endEmpId+" " + LoginFreelancerFragment._id  + "id");
        if (AuthActivity.isEmployer || LoginEmployerFragment.isEmployer){
            uniqueId = endEmpId;
        }else{
            uniqueId = endFlcId;
        }
        Log.d(TAG, "uniqueId " + uniqueId);
        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }
        getContentMessage();
        Log.d("username: ", " user name: " + LoginEmployerFragment.empName +" " + AuthActivity.empName);
        Log.d("username: ", " user name free: " + LoginFreelancerFragment.flcName +" " + AuthActivity.flcName);

        if (AuthActivity.isEmployer || LoginEmployerFragment.isEmployer){
            if (LoginEmployerFragment.empName != null){
                Username = LoginEmployerFragment.empName;
            }else {
                Username = AuthActivity.empName;
            }
        }else {
            if (LoginFreelancerFragment.flcName != null){
                Username = LoginFreelancerFragment.flcName;
            }else {
                Username = AuthActivity.flcName;
            }
        }
        String ds;
        if(hasConnection){

        }else {
            mSocket.connect();
            JSONObject room = new JSONObject();
            try {
                room.put("room", _id);
                mSocket.emit("join", room);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.on("connect user", onNewUser);
            mSocket.on("chat message", onNewMessage);
           // mSocket.on("on typing", onTyping);

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " Connected");
                mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "onCreate: " + hasConnection);
        hasConnection = true;

        textField = view.findViewById(R.id.textField);
        sendButton = view.findViewById(R.id.sendButton);

        messageListView = view.findViewById(R.id.messageListView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

//
        List<MessageFormat> messageFormatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), R.layout.item_chat, messageFormatList);
        messageListView.setAdapter(chatAdapter);

        //onTypeButtonEnable();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasConnection", hasConnection); //
    }

    private void getContentMessage(){
        customProgressBar.show();
        final ArrayList<MessageFormat> messageFormatArrayList = new ArrayList<>();
        API api = retrofitClient.getClien().create(API.class);
        api.getMessageDetail(_id, accessTokenDb).enqueue(new Callback<vn.com.gojobs.Model.Message>() {
            @Override
            public void onResponse(Call<vn.com.gojobs.Model.Message> call, Response<vn.com.gojobs.Model.Message> response) {
                if (response.code() == 200){
                    vn.com.gojobs.Model.Message messages = response.body();
                    messageFormatArrayList.addAll(messages.getContent());
                    if (messageFormatArrayList.size() != 0){
                        chatAdapter = new ChatAdapter(getActivity(), R.layout.item_chat, messageFormatArrayList);
                        messageListView.setAdapter(chatAdapter);
                    }
                    Log.d(TAG, "liss " + messageFormatArrayList);
                    emp = messages.getEmpId().get_id();
                    flc = messages.getFlcId().get_id();
                    if (AuthActivity.isEmployer){
                        Username = messages.getEmpId().getEmpName();
                    }else {
                        Username = messages.getFlcId().getFlcName();
                    }
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<vn.com.gojobs.Model.Message> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }

//
    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    String id;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                        id = data.getString("userId");

                        Log.i(TAG, "run: " + username + message + id);

                        MessageFormat format = new MessageFormat(id, username, message);
                        Log.i(TAG, "run:4 ");
                        chatAdapter.add(format);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userName", username);
                        jsonObject.put("message", message);
                        jsonObject.put("userId", id);

                        formatList.put(jsonObject);
                        Log.d(TAG, formatList + " kasm");
//
                        Log.i(TAG, "run:5 ");

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    Emitter.Listener onNewUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int length = args.length;

                    if(length == 0){
                        return;
                    }
                    //Here i'm getting weird error..................///////run :1 and run: 0
                    Log.i(TAG, "run: ");
                    Log.i(TAG, "run: " + args.length);
                    String username =args[0].toString();
                    try {

                        JSONObject object = new JSONObject(username);
                        username = object.getString("username");
                        Log.d(TAG, "user " + username);
                        if (username.contains("DisConnected")){
                            formatList = new JSONArray();
                        }
                        Log.d(TAG, "mmm "+ formatList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MessageFormat format = new MessageFormat(null, username,null);
                    chatAdapter.add(format);

                    messageListView.smoothScrollToPosition(0);
                    messageListView.scrollTo(0, chatAdapter.getCount()-1);
                    Log.i(TAG, "run: " + username);
                }
            });
        }
    };

    public void sendMessage(){
        Log.i(TAG, "sendMessage: ");
        String message = textField.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            Log.i(TAG, "sendMessage:2 ");
            return;
        }
        textField.setText("");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("username", Username);
            jsonObject.put("userId", uniqueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "sendMessage: 1"+ mSocket.emit("chat message", jsonObject));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (getActivity().isFinishing()) {
        Log.i(TAG, "onDestroy: ");

        JSONObject userId = new JSONObject();
        try {
            userId.put("username", Username + " DisConnected");
            mSocket.emit("connect user", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (formatList.length() != 0){
            API api = retrofitClient.getClien().create(API.class);
            api.newMessage1(emp, flc,formatList, accessTokenDb).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code()==200){
                        Log.d(TAG, "code ok ");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

        formatList = new JSONArray();
        Log.d(TAG,"id: " + empId + idFlc);

        Log.d(TAG, "onDestroyView: ondestroy view");
     //   mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
        mSocket.off("connect user", onNewUser);
        //mSocket.off("on typing", onTyping);
        Username = "";
        chatAdapter.clear();

        Log.d(TAG, "list: " + formatList);

    }
}