package id.co.rumahraga.ui.adapters.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.co.rumahraga.R;


import id.co.rumahraga.model.TransactionDetailModel;

import java.util.List;

public class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailAdapter.ViewHolder> {
    private Context context;
    private List<TransactionDetailModel> detailTransactionModelList;


    public TransactionDetailAdapter(Context context, List<TransactionDetailModel> detailTransactionModelList) {
        this.context = context;
        this.detailTransactionModelList = detailTransactionModelList;
    }



    @NonNull
    @Override
    public TransactionDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_booked, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDetailAdapter.ViewHolder holder, int position) {

        holder.tvJam.setText(detailTransactionModelList.get(holder.getBindingAdapterPosition()).getJam());
        holder.tvDate.setText(detailTransactionModelList.get(holder.getBindingAdapterPosition()).getOrder_date());
        holder.tvFieldName.setText(detailTransactionModelList.get(holder.getBindingAdapterPosition()).getField_name());

        if (detailTransactionModelList.get(holder.getBindingAdapterPosition()).getStatus() == 1) {
            holder.tvFieldName.setTextColor(context.getColor(R.color.main));
        }else if (detailTransactionModelList.get(holder.getBindingAdapterPosition()).getStatus() == 2) {
            holder.tvFieldName.setTextColor(context.getColor(R.color.yellow));
        }else {
            holder.tvFieldName.setTextColor(context.getColor(R.color.red));

        }



    }

    @Override
    public int getItemCount() {
        return detailTransactionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvJam, tvFieldName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);

        }
    }
}
