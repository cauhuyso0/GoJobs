package vn.com.gojobs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.com.gojobs.Model.Contract;
import vn.com.gojobs.Model.GojobConfig;
import vn.com.gojobs.R;
import vn.com.gojobs.interfake.IItemRowClickedCallback;
import vn.com.gojobs.interfake.ItemRowClickedCallbackForContract;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {

    Context context;
    ArrayList<Contract> dsContract;
    LayoutInflater layoutInflater;
    IItemRowClickedCallback iItemRowClickedCallback;
    ItemRowClickedCallbackForContract itemRowClickedCallbackForContract;

    public ContractAdapter(Context context, ArrayList<Contract> ds3, IItemRowClickedCallback iItemRowClickedCallback, ItemRowClickedCallbackForContract itemRowClickedCallbackForContract)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.dsContract = ds3;
        this.iItemRowClickedCallback = iItemRowClickedCallback;
        this.itemRowClickedCallbackForContract = itemRowClickedCallbackForContract;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgAvtFreelancer;
        public TextView tvTenFreelancer, tvLuongFreelancer, tvChapNhan, tvTuChoi;
        private LinearLayout lnParent;

        public  ViewHolder(View itemView) {
            super(itemView);

            lnParent = itemView.findViewById(R.id.ln_parent_contract);
            imgAvtFreelancer = itemView.findViewById(R.id.img_avatar_freelancer_contract);
            tvTenFreelancer = itemView.findViewById(R.id.tv_ten_freelancer_contract);
            tvLuongFreelancer= itemView.findViewById(R.id.tv_luong_contract);
            tvChapNhan = itemView.findViewById(R.id.tv_chap_nhan_contract);
            tvTuChoi = itemView.findViewById(R.id.tv_tu_choi_contract);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.contract_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Contract contract = dsContract.get(position);

            if (contract.getFlcId().getFlcName().equals("")){
                holder.tvTenFreelancer.setText("No name");
            } else {
                holder.tvTenFreelancer.setText(contract.getFlcId().getFlcName() + "");
            }

            holder.tvLuongFreelancer.setText(contract.getJobTotalSalaryPerHeadCount()+"");

            if (contract.getContractStatus().equals(GojobConfig.STATUS_JOB_APPROVED)){
                holder.tvChapNhan.setText("Hoàn thành");
                holder.tvTuChoi.setText("Hủy");
                holder.tvChapNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemRowClickedCallbackForContract.onItemRowClickedd(GojobConfig.STATUS_JOB_APPROVED,contract.get_id());
                    }
                });

                holder.tvTuChoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemRowClickedCallbackForContract.onItemRowClickedd("CANCELED_JOB",contract.get_id());

                    }
                });
            }else if (contract.getContractStatus().equals(GojobConfig.STATUS_JOB_ACCEPTED)){
                holder.tvChapNhan.setText("Bắt đầu");
                holder.tvChapNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemRowClickedCallbackForContract.onItemRowClickedd(GojobConfig.STATUS_JOB_ACCEPTED, contract.get_id());

                    }
                });
                holder.tvTuChoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemRowClickedCallbackForContract.onItemRowClickedd("",contract.get_id());

                    }
                });
            }else if (contract.getContractStatus().equals(GojobConfig.STATUS_JOB_APPLIED)){
                holder.tvChapNhan.setText("Chấp nhận");
                holder.tvChapNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemRowClickedCallbackForContract.onItemRowClickedd(GojobConfig.STATUS_JOB_APPLIED, contract.get_id());

                    }
                });
                holder.tvTuChoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                itemRowClickedCallbackForContract.onItemRowClickedd("",contract.get_id());

                    }
                });
            }
            else {
                holder.tvChapNhan.setVisibility(View.INVISIBLE);
                holder.tvTuChoi.setVisibility(View.INVISIBLE);
            }

            holder.lnParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iItemRowClickedCallback.onButtonClicked(contract.getFlcId().get_id());
                }
            });
//        holder.imgAvtFreelancer.setText(ds3.get(position).getJobTitle());

    }

    @Override
    public int getItemCount() {
        return dsContract.size();
    }

}
