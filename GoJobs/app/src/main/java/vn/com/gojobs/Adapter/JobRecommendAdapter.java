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

import vn.com.gojobs.Fragment.JobDetailFragment;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class JobRecommendAdapter extends RecyclerView.Adapter<JobRecommendAdapter.ViewHolder> {

    static Context context;
    ArrayList<Job> ds;
    IItemRowClickedCallback iItemRowClickedCallback;

    public JobRecommendAdapter(Context context, ArrayList<Job> ds, IItemRowClickedCallback iItemRowClickedCallback)
    {
        this.context=context;
        this.ds = ds;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivJobImage;
        public TextView tvJobTitle, tvJobSalary, tvJobAddress, tvJobDistance;
        public  ViewHolder(View itemView) {
            super(itemView);
            ivJobImage=(ImageView)itemView.findViewById(R.id.ivJobImage);
            tvJobTitle=(TextView)itemView.findViewById(R.id.tvJobTitle);
            tvJobSalary=(TextView)itemView.findViewById(R.id.tvJobSalary);
            tvJobAddress=(TextView)itemView.findViewById(R.id.tvJobAddress);
            tvJobDistance=(TextView)itemView.findViewById(R.id.tvDistance);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context).inflate(R.layout.job_recommend_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do

        final Job jobRecommend = ds.get(position);

//        holder.ivJobImage.setImageResource(jobRecommend.image);
//        holder.tvJobTitle.setText(jobRecommend.title);
//        holder.tvJobSalary.setText(jobRecommend.salary);
//        holder.tvJobAddress.setText(jobRecommend.address);
//        holder.tvJobDistance.setText(jobRecommend.distance);

        // set su kien onClick cho cardview
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iItemRowClickedCallback.onButtonClicked(JobDetailFragment.TAG);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
