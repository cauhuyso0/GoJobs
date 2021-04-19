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

import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class FreelancerNearAdapter extends RecyclerView.Adapter<FreelancerNearAdapter.ViewHolder> {

    static Context context;
    ArrayList<Freelancer> dsFreelancerNear;
    IItemRowClickedCallback iItemRowClickedCallback;

    public FreelancerNearAdapter(Context context, ArrayList<Freelancer> dsFreelancerNear, IItemRowClickedCallback iItemRowClickedCallback)
    {
        this.context=context;
        this.dsFreelancerNear = dsFreelancerNear;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivFreelancerAvatar;
        public TextView tvFreelancerName, tvFreelancerMajor;
        public  ViewHolder(View itemView) {
            super(itemView);

            ivFreelancerAvatar=(ImageView)itemView.findViewById(R.id.ivFreelancerAvatar1);
            tvFreelancerName=(TextView)itemView.findViewById(R.id.tvFreelancerName1);
            tvFreelancerMajor=(TextView)itemView.findViewById(R.id.tvFreelancerMajor1);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context).inflate(R.layout.near_freelancer_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do
        final Freelancer freelancer = dsFreelancerNear.get(position);

//        holder.ivFreelancerAvatar.setImageResource(dsFreelancerNear.get(position).avatar);
        holder.tvFreelancerName.setText(dsFreelancerNear.get(position).getFlcName());
        holder.tvFreelancerMajor.setText(dsFreelancerNear.get(position).getFlcMajor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iItemRowClickedCallback.onButtonClicked(freelancer.get_id()+"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return dsFreelancerNear.size();
    }

}
