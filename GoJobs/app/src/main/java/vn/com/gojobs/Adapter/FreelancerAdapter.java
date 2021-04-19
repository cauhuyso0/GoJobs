package vn.com.gojobs.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

public class FreelancerAdapter extends RecyclerView.Adapter<FreelancerAdapter.ViewHolder> {

    static Context context;
    ArrayList<Freelancer> dsFreelancer;
    IItemRowClickedCallback iItemRowClickedCallback;

    public FreelancerAdapter(Context context, ArrayList<Freelancer> dsFreelancer, IItemRowClickedCallback iItemRowClickedCallback)
    {
        this.context=context;
        this.dsFreelancer = dsFreelancer;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivFreelancerAvatar;
        public TextView tvFreelancerName, tvFreelancerMajor, tvFreelancerAddress, tvFreelancerDistance;
        public RatingBar rbFreelancer;


        public  ViewHolder(View itemView) {
            super(itemView);
            ivFreelancerAvatar=(ImageView)itemView.findViewById(R.id.ivFreelancerAvatar);
            tvFreelancerName=(TextView)itemView.findViewById(R.id.tvFreelancerName);
            tvFreelancerMajor=(TextView)itemView.findViewById(R.id.tvFreelancerMajor);
            tvFreelancerAddress=(TextView)itemView.findViewById(R.id.tvFreelancerAddress);
            tvFreelancerDistance=(TextView)itemView.findViewById(R.id.tvFreelancerDistance);
            rbFreelancer = (RatingBar) itemView.findViewById(R.id.rbFreelancer);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context).inflate(R.layout.freelancer_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do

        final Freelancer freelancer = dsFreelancer.get(position);

        if (freelancer.getFlcAvatar() != null) {
            String avatar = freelancer.getFlcAvatar();
            // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.ivFreelancerAvatar.setImageBitmap(decodedByte);
        }
//        Picasso.with(context).load(dsFreelancer.get(position).getFlcAvatar()).into(holder.ivFreelancerAvatar);
        holder.tvFreelancerName.setText(dsFreelancer.get(position).getFlcName());
        holder.tvFreelancerMajor.setText(dsFreelancer.get(position).getFlcMajor());
        holder.tvFreelancerAddress.setText(dsFreelancer.get(position).getFlcAddress());
        holder.rbFreelancer.setRating(dsFreelancer.get(position).getFlcRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iItemRowClickedCallback.onButtonClicked(freelancer.get_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsFreelancer.size();
    }

}
