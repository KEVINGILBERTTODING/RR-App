package com.example.rumahraga.ui.adapters.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahraga.R;
import com.example.rumahraga.model.TransactionDetailModel;

import java.util.List;

public class TransactionTicketAdapter extends RecyclerView.Adapter<TransactionTicketAdapter.ViewHolder> {
    private Context context;
    private List<TransactionDetailModel> detailTransactionModelList;


    public TransactionTicketAdapter(Context context, List<TransactionDetailModel> detailTransactionModelList) {
        this.context = context;
        this.detailTransactionModelList = detailTransactionModelList;
    }



    @NonNull
    @Override
    public TransactionTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionTicketAdapter.ViewHolder holder, int position) {

        holder.tvJam.setText(detailTransactionModelList.get(holder.getBindingAdapterPosition()).getJam());
        holder.tvDate.setText(detailTransactionModelList.get(holder.getBindingAdapterPosition()).getOrder_date());





    }

    @Override
    public int getItemCount() {
        return detailTransactionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvJam;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvJam = itemView.findViewById(R.id.tvJam);

        }
    }
}
