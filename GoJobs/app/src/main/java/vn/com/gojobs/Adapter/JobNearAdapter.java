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

public class JobNearAdapter extends RecyclerView.Adapter<JobNearAdapter.ViewHolder> {

    static Context context;
    ArrayList<Job> ds1;
    IItemRowClickedCallback iItemRowClickedCallback;
    public JobNearAdapter(Context context, ArrayList<Job> ds1, IItemRowClickedCallback iItemRowClickedCallback)
    {
        this.context=context;
        this.ds1 = ds1;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivJobImage;
        public TextView tvJobTitle, tvJobSalary, tvJobDistance;
        public  ViewHolder(View itemView) {
            super(itemView);
            ivJobImage=(ImageView)itemView.findViewById(R.id.ivJobImage1);
            tvJobTitle=(TextView)itemView.findViewById(R.id.tvJobTitle1);
            tvJobSalary=(TextView)itemView.findViewById(R.id.tvJobSalary1);
            tvJobDistance=(TextView)itemView.findViewById(R.id.tvDistance1);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context).inflate(R.layout.near_job_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do
//        holder.ivJobImage.setImageResource(ds1.get(position).image);
//        holder.tvJobTitle.setText(ds1.get(position).title.toString());
//        holder.tvJobSalary.setText(ds1.get(position).salary.toString());
//        holder.tvJobDistance.setText(ds1.get(position).distance.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iItemRowClickedCallback.onButtonClicked(JobDetailFragment.TAG);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
