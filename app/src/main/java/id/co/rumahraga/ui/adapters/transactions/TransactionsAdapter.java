package id.co.rumahraga.ui.adapters.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import id.co.rumahraga.R;

import id.co.rumahraga.model.TransactionModel;
import id.co.rumahraga.model.listener.ItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private Context context;
    private List<TransactionModel> transactionModels;
    private ItemClickListener itemClickListener;

    public TransactionsAdapter(Context context, List<TransactionModel> transactionModels) {
        this.context = context;
        this.transactionModels = transactionModels;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_transactions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
        holder.tvDate.setText(transactionModels.get(holder.getBindingAdapterPosition()).getCreated_at());
        holder.tvFieldName.setText(transactionModels.get(holder.getBindingAdapterPosition()).getField_name());


        if (transactionModels.get(holder.getBindingAdapterPosition()).getStatus() == 1) {
            holder.tvStatus.setText("Berhasil");

            if (transactionModels.get(holder.getBindingAdapterPosition()).getReview_id() != null) {
                holder.lrReview.setVisibility(View.GONE);
            }else{
                holder.lrReview.setVisibility(View.VISIBLE);
            }
            holder.tvStatus.setTextColor(context.getColor(R.color.main));

            holder.tvTotalPrice.setTextColor(context.getColor(R.color.main));


        }else if (transactionModels.get(holder.getBindingAdapterPosition()).getStatus() == 2) {
            holder.tvStatus.setText("Menunggu Validasi");
            holder.tvStatus.setTextColor(context.getColor(R.color.yellow));
            holder.tvTotalPrice.setTextColor(context.getColor(R.color.yellow));
            holder.lrReview.setVisibility(View.GONE);
        }else if (transactionModels.get(holder.getBindingAdapterPosition()).getStatus() == 0) {
            holder.tvStatus.setText("Gagal");
            holder.tvStatus.setTextColor(context.getColor(R.color.red));
            holder.lrReview.setVisibility(View.GONE);
            holder.tvTotalPrice.setTextColor(context.getColor(R.color.red));


        }


        Glide.with(context).load(transactionModels.get(holder.getBindingAdapterPosition()).getField_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).into(holder.ivField);


        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        holder.tvTotalPrice.setText("Rp. " + decimalFormat.format(transactionModels.get(holder.getBindingAdapterPosition()).getTotal_price()));

    }

    public void filter(ArrayList<TransactionModel> transactionModels ){
        this.transactionModels = transactionModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName, tvStatus, tvDate, tvTotalPrice;
        ImageView ivField;
        LinearLayout lrReview;
        private Button btnReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivField = itemView.findViewById(R.id.ivField);
            lrReview = itemView.findViewById(R.id.lrReview);
            btnReview = itemView.findViewById(R.id.btnReview);

            btnReview.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("review", getBindingAdapterPosition(), transactionModels.get(getBindingAdapterPosition()));
                }
            });



            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("transaction", getBindingAdapterPosition(), transactionModels.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
