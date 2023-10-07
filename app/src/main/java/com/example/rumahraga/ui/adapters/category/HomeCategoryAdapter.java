package com.example.rumahraga.ui.adapters.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.model.CategoryModel;
import com.example.rumahraga.model.listener.ItemClickListener;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> categoryModels;
    private ItemClickListener itemClickListener;

    public HomeCategoryAdapter(Context context, List<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(categoryModels.get(holder.getAdapterPosition()).getName());
        Glide.with(context).load(categoryModels.get(holder.getAdapterPosition()).getImage())
                .override(300, 300).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                .into(holder.ivCategory);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategories);
            ivCategory = itemView.findViewById(R.id.ivCategory);
        }
    }
}
