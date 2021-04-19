package vn.com.gojobs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.com.gojobs.Model.Notification;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.ItemRowClickedCallbackForNotification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    static Context context;
    public ArrayList<Notification> dsNoti;
    ItemRowClickedCallbackForNotification itemRowClickedCallbackForNotification;

    public NotificationAdapter(Context context, ArrayList<Notification> dsNoti, ItemRowClickedCallbackForNotification itemRowClickedCallbackForNotification)
    {
        this.context=context;
        this.dsNoti = dsNoti;
        this.itemRowClickedCallbackForNotification = itemRowClickedCallbackForNotification;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivNotiIcon;
        public TextView tvNotiContent;
        public  ViewHolder(View itemView) {
            super(itemView);
            ivNotiIcon=(ImageView)itemView.findViewById(R.id.NotiIcon);
            tvNotiContent=(TextView)itemView.findViewById(R.id.tvNotiContent);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do
        final Notification notification = dsNoti.get(position);

        holder.tvNotiContent.setText(notification.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemRowClickedCallbackForNotification.onItemRowClicked(notification.getJobId().get_id(), notification.getContent(), notification.getEmpId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return dsNoti.size();
    }

}
