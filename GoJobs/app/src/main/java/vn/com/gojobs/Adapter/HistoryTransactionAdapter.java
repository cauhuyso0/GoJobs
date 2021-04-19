package vn.com.gojobs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.com.gojobs.Model.Receipt;
import vn.com.gojobs.R;

public class HistoryTransactionAdapter extends RecyclerView.Adapter<HistoryTransactionAdapter.ViewHolder>{

    Context context;
    ArrayList<Receipt> historyTransactions;

    public HistoryTransactionAdapter(Context context, ArrayList<Receipt> dsContract) {
        this.context = context;
        this.historyTransactions = dsContract;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.transaction_history_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Receipt receipt = historyTransactions.get(position);

        holder.tvNgayGiaoDich.setText(receipt.getCreatedAt());
        holder.tvSoTienGiaoDich.setText(receipt.getUpdatedValue());

    }

    @Override
    public int getItemCount() {
        return historyTransactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNgayGiaoDich, tvSoTienGiaoDich;

        public  ViewHolder(View itemView) {
            super(itemView);
            tvNgayGiaoDich = itemView.findViewById(R.id.tv_ngay_giao_dich_lsgd);
            tvSoTienGiaoDich = itemView.findViewById(R.id.tv_so_tien_gd_lsgd);
        }
    }

}
