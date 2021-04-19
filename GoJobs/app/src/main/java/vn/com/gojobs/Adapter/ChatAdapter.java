package vn.com.gojobs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.com.gojobs.Fragment.ChatFragment;
import vn.com.gojobs.Model.MessageFormat;
import vn.com.gojobs.R;

public class ChatAdapter  extends ArrayAdapter<MessageFormat> {


    public ChatAdapter(Context context, int resource, List<MessageFormat> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageFormat message = getItem(position);

        if (message != null){
            if(TextUtils.isEmpty(message.getMessage())){


                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_connected, parent, false);

                TextView messageText = convertView.findViewById(R.id.message_body);

                String userConnected = message.getUsername();
                messageText.setText(userConnected);

            }else if(message.getUserId().equals(ChatFragment.uniqueId)){ //ChatFragment.uniqueId

                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);
                TextView messageText = convertView.findViewById(R.id.message_body);
                messageText.setText(message.getMessage());

            }else {

                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.their_message, parent, false);

                TextView messageText = convertView.findViewById(R.id.message_body);
                TextView usernameText = (TextView) convertView.findViewById(R.id.name_their);

                messageText.setVisibility(View.VISIBLE);
                usernameText.setVisibility(View.VISIBLE);

                messageText.setText(message.getMessage());
                usernameText.setText(message.getUsername());

            }
        }


        return convertView;
    }

}
