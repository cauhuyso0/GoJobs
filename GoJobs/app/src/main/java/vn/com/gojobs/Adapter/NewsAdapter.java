package vn.com.gojobs.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.com.gojobs.Model.News;
import vn.com.gojobs.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    static Context context;
    ArrayList<News> ds2;
    public NewsAdapter(Context context, ArrayList<News> ds2)
    {
        this.context=context;
        this.ds2 = ds2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivBackgroundNews;
        public TextView tvContentNews;
        public ImageView ivLikeNews;
        public ImageView ivShareNews;
        public  ViewHolder(View itemView) {
            super(itemView);
            ivBackgroundNews=(ImageView)itemView.findViewById(R.id.ivBackgroundNews);
            tvContentNews=(TextView)itemView.findViewById(R.id.tvContentNews);
            ivLikeNews=(ImageView)itemView.findViewById(R.id.ivLikeNews);
            ivShareNews=(ImageView)itemView.findViewById(R.id.ivShareNews);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tao view va gan layout vao view

        View v = LayoutInflater.from(context)
                .inflate(R.layout.news_item, parent, false);
        // gan cac thuoc tinh nhu size, margins, paddings.....
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - lay  phan tu tu danh sach du lieu tai vi tri position
        // - thay the noi dung cua view voi phan tu do
        holder.tvContentNews.setText(ds2.get(position).content.toString());
        holder.ivBackgroundNews.setImageResource(ds2.get(position).image);
        holder.ivBackgroundNews.setTag(ds2.get(position).image);
        holder.ivLikeNews.setImageResource(ds2.get(position).likeicon);

        holder.ivBackgroundNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Bạn vừa click vào tin " + (position+1), Toast.LENGTH_SHORT).show();
            }
        });


        holder.ivLikeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ds2.get(position).likeicon== R.drawable.ic_liked) {
                    ds2.get(position).likeicon=R.drawable.ic_like;
                    holder.ivLikeNews.setImageResource(ds2.get(position).likeicon);
                }
                else if(ds2.get(position).likeicon==R.drawable.ic_like) {
                    ds2.get(position).likeicon=R.drawable.ic_liked;
                    holder.ivLikeNews.setImageResource(ds2.get(position).likeicon);
                }

            }
        });

        holder.ivShareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + context.getResources().getResourcePackageName(holder.ivShareNews.getId())
                        + '/' + "drawable" + '/' +
                        context.getResources().getResourceEntryName((int)holder.ivShareNews.getTag()));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                shareIntent.setType("image/jpeg");
                context.startActivity(Intent.createChooser(shareIntent, "hello"));
            }
        });

    }


    @Override
    public int getItemCount() {
        return ds2.size();
    }
}
