package com.example.rumahraga.ui.adapters.jam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahraga.R;
import com.example.rumahraga.model.JamModel;
import com.example.rumahraga.model.listener.ItemClickListener;

import java.util.List;

public class JamAdapter extends RecyclerView.Adapter<JamAdapter.ViewHolder> {
    private Context context;
    private List<JamModel> jamModelList;
    private ItemClickListener itemClickListener;

    public JamAdapter(Context context, List<JamModel> jamModelList) {
        this.context = context;
        this.jamModelList = jamModelList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public JamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_jam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JamAdapter.ViewHolder holder, int position) {
        holder.tvJam.setText(jamModelList.get(holder.getBindingAdapterPosition()).getJam());

        if (jamModelList.get(holder.getBindingAdapterPosition()).getIs_available() == 0) {
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.red));
        }else if (jamModelList.get(holder.getBindingAdapterPosition()).getIs_available() == 1){
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.main2));

            holder.itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("hour", holder.getBindingAdapterPosition(), jamModelList.get(holder.getBindingAdapterPosition()));
                }
            });
        }else if (jamModelList.get(holder.getBindingAdapterPosition()).getIs_available() == 2){
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.yellow));

            holder.itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("hour", holder.getBindingAdapterPosition(), jamModelList.get(holder.getBindingAdapterPosition()));
                }
            });
        }
        

    }

    @Override
    public int getItemCount() {
        return jamModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJam;
        private CardView cvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJam = itemView.findViewById(R.id.tvJam);
            cvStatus = itemView.findViewById(R.id.cvStatus);
        }
    }
}
