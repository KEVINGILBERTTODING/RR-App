package com.example.rumahraga.ui.adapters.booked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahraga.R;
import com.example.rumahraga.model.BookedModel;
import com.example.rumahraga.model.listener.ItemClickListener;

import java.util.List;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ViewHolder> {
    private Context context;
    private List<BookedModel> bookedModelList;
    private ItemClickListener itemClickListener;

    public BookedAdapter(Context context, List<BookedModel> bookedModelList) {
        this.context = context;
        this.bookedModelList = bookedModelList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BookedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_booked, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedAdapter.ViewHolder holder, int position) {

        holder.tvJam.setText(bookedModelList.get(holder.getBindingAdapterPosition()).getJam());
        holder.tvDate.setText(bookedModelList.get(holder.getBindingAdapterPosition()).getOrder_date());



    }

    @Override
    public int getItemCount() {
        return bookedModelList.size();
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
