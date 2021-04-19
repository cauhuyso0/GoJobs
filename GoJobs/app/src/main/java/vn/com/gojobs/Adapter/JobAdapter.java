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

import vn.com.gojobs.Model.Job;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    static Context context;
    ArrayList<Job> ds3;
    LayoutInflater layoutInflater;
    IItemRowClickedCallback iItemRowClickedCallback;

    public JobAdapter(Context context, ArrayList<Job> ds3, IItemRowClickedCallback iItemRowClickedCallback)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.ds3 = ds3;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivJobImage;
        public TextView tvJobTitle, tvJobStatus, tvJobPayment, tvJobSalary, tvJobDateStart, tvCountFlc, tvJobDateEnd;

        public  ViewHolder(View itemView) {
            super(itemView);

            ivJobImage = itemView.findViewById(R.id.imgProfile);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvJobPayment = itemView.findViewById(R.id.tv_job_payment_type);
            tvJobSalary= itemView.findViewById(R.id.tv_job_salary);
            tvCountFlc = itemView.findViewById(R.id.tv_so_luong_ung_vien);
            tvJobDateStart = itemView.findViewById(R.id.tv_job_date_start);
            tvJobDateEnd = itemView.findViewById(R.id.tv_job_date_end);
            tvJobStatus= itemView.findViewById(R.id.tv_job_status);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.job_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Job job = ds3.get(position);

        if (job != null) {

            final String idJob = job.get_id();

            if (job.getEmpId().getEmpLogo() != null) {
                String avatar = job.getEmpId().getEmpLogo();
                // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                holder.ivJobImage.setImageBitmap(decodedByte);
            }

            holder.tvJobTitle.setText(ds3.get(position).getJobTitle());
            holder.tvJobPayment.setText(ds3.get(position).getJobPaymentType());
            holder.tvJobSalary.setText(ds3.get(position).getJobSalary() + "");
            holder.tvJobDateStart.setText(ds3.get(position).getJobStart() + "");
            holder.tvJobDateEnd.setText(ds3.get(position).getJobEnd() + "");
            holder.tvJobStatus.setText(ds3.get(position).getJobStatus());
            holder.tvCountFlc.setText(ds3.get(position).getJobHeadCountTarget() + "");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iItemRowClickedCallback.onButtonClicked(idJob);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return ds3.size();
    }

}
