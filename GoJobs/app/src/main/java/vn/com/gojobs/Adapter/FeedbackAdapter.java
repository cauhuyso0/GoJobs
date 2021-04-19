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

import java.util.ArrayList;

import vn.com.gojobs.Model.Feedback;
import vn.com.gojobs.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{

    static Context context;
    ArrayList<Feedback> dsFeedback;

    public FeedbackAdapter(Context context, ArrayList<Feedback> dsFeedback)
    {
        this.context=context;
        this.dsFeedback = dsFeedback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feedback_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new FeedbackAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Feedback feedback = dsFeedback.get(position);

        if (feedback.getEmpId() != null) {
            String avatar = feedback.getEmpId().getEmpLogo();
            // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.ivFreelancerAvatar.setImageBitmap(decodedByte);
            holder.tvTitleFeedback.setText(feedback.getEmpId().getEmpName());

        } else {
            String avatar = feedback.getFlcId().getFlcAvatar();
            // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.ivFreelancerAvatar.setImageBitmap(decodedByte);
            holder.tvTitleFeedback.setText(feedback.getFlcId().getFlcName());
        }

        holder.rbFreelancer.setNumStars(feedback.getStarRating());
    }

    @Override
    public int getItemCount() {
        return dsFeedback.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivFreelancerAvatar;
        public TextView tvTitleFeedback;
        public RatingBar rbFreelancer;
        public  ViewHolder(View itemView) {
            super(itemView);
            ivFreelancerAvatar=(ImageView)itemView.findViewById(R.id.img_avatar_feedback);
            tvTitleFeedback=(TextView)itemView.findViewById(R.id.txt_title_feedback);
            rbFreelancer = (RatingBar) itemView.findViewById(R.id.rb_star);
        }
    }

}
