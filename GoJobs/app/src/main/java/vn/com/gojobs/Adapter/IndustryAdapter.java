package vn.com.gojobs.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import vn.com.gojobs.Freelancer.FreelancerIndustryFragment;
import vn.com.gojobs.Model.Industry;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;

import static android.content.Context.MODE_PRIVATE;

public class IndustryAdapter extends BaseAdapter {

    IItemRowClickedCallback iItemRowClickedCallback;
    private ArrayList<Industry> industries;
    private LayoutInflater layoutInflater;
    private Context context;
    private SharedPreferences sharedPreferences;
    private final String SHARE_PREF_NAME = "mypref";

    public IndustryAdapter(Context context, ArrayList<Industry> industries, IItemRowClickedCallback iItemRowClickedCallback) {
        this.iItemRowClickedCallback = iItemRowClickedCallback;
        this.context = context;
        this.industries = industries;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return industries.size();
    }

    @Override
    public Object getItem(int i) {
        return industries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.indutry_item, parent, false);
            holder = new ViewHolder();
            holder.imgClick = convertView.findViewById(R.id.img_click);
            holder.title = convertView.findViewById(R.id.tvIndustryName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Industry industry = industries.get(position);
        holder.title.setText("" + industry.getIndustryName());
//        sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
//
//        Set<String> set = sharedPreferences.getStringSet("list_filter", null);
//        if (set != null) {
            for (int i = 0; i < FreelancerIndustryFragment.listFilter.size(); i++){
                if (FreelancerIndustryFragment.listFilter.get(i).equals(industry.getIndustryName())){
                    holder.imgClick.setVisibility(View.VISIBLE);

                }
            }
//        }

        convertView.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    holder.imgClick.setVisibility(View.VISIBLE);

                    FreelancerIndustryFragment.listFilter.add(industry.getIndustryName());
                    Log.d("test", "onAdd: " + FreelancerIndustryFragment.listFilter);
                    count++;
                } else {
                    holder.imgClick.setVisibility(View.GONE);
                    count--;
                    if (FreelancerIndustryFragment.listFilter.size() > 0){
                        for (int i = 0; i < FreelancerIndustryFragment.listFilter.size(); i++){
                            if (FreelancerIndustryFragment.listFilter.get(i).equals(industry.getIndustryName())){

                                FreelancerIndustryFragment.listFilter.remove(i);
                                Log.d("test", "onRemove: " + FreelancerIndustryFragment.listFilter);
                            }
                        }
                    }
                }
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        TextView title;
        ImageView imgClick;
    }
}
