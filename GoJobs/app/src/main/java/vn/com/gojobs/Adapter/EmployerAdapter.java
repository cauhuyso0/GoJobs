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

import de.hdodenhof.circleimageview.CircleImageView;
import vn.com.gojobs.Model.Employer;
import vn.com.gojobs.R;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.ViewHolder> {

    static Context context;
    ArrayList<Employer> dsEmployer;

    public EmployerAdapter(Context context, ArrayList<Employer> dsEmployer) {
        this.context = context;
        this.dsEmployer = dsEmployer;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgEmployerAvatar;
        public TextView tvEmployerName, tvEmployerSdt, tvEmployerAddress;
        public RatingBar rbEmployer;

        public ViewHolder(View itemView) {
            super(itemView);
            imgEmployerAvatar = itemView.findViewById(R.id.img_avatar_employer);
            tvEmployerName = itemView.findViewById(R.id.tv_employer_name);
            tvEmployerSdt = itemView.findViewById(R.id.tv_employer_sdt);
            tvEmployerAddress = itemView.findViewById(R.id.tv_employer_address);
            rbEmployer = itemView.findViewById(R.id.rb_employer);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context).inflate(R.layout.employer_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do

        Employer employer = dsEmployer.get(position);

        if (employer.getEmpLogo() != null) {
            String avatar = employer.getEmpLogo();
            // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imgEmployerAvatar.setImageBitmap(decodedByte);
        }

        holder.tvEmployerName.setText(employer.getEmpName());
        holder.tvEmployerSdt.setText(employer.getEmpPhone());
        holder.tvEmployerAddress.setText(employer.getEmpAddress());
        holder.rbEmployer.setRating(employer.getEmpRating());
    }

    @Override
    public int getItemCount() {
        return dsEmployer.size();
    }

}
