package vn.com.gojobs.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.com.gojobs.Model.Message;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    static Context context;
    public ArrayList<Message> dsMess;
    private IItemRowClickedCallback iItemRowClickedCallback;

    public MessageAdapter(Context context, ArrayList<Message> dsMess, IItemRowClickedCallback iItemRowClickedCallback) {
        this.context = context;
        this.dsMess = dsMess;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMessIcon;
        public TextView tvMessContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ivMessIcon = (ImageView) itemView.findViewById(R.id.MessIcon);
            tvMessContent = (TextView) itemView.findViewById(R.id.tvMessContent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do
        final Message message = dsMess.get(position);

        if (message.getFlcId() != null) {

            if (message.getFlcId().getFlcAvatar() != null) {
                String avatar = message.getFlcId().getFlcAvatar();
                // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                holder.ivMessIcon.setImageBitmap(decodedByte);
            }

            holder.tvMessContent.setText("Bạn có tin nhắn mới từ " + message.getFlcId().getFlcName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iItemRowClickedCallback.onButtonClicked(message.get_id());
                }
            });
        } else {

            if (message.getEmpId().getEmpLogo() != null) {
                String avatar = message.getEmpId().getEmpLogo();
                // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                holder.ivMessIcon.setImageBitmap(decodedByte);
            }

            holder.tvMessContent.setText("Bạn có tin nhắn mới từ " + message.getEmpId().getEmpName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iItemRowClickedCallback.onButtonClicked(message.get_id());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dsMess.size();
    }

}
