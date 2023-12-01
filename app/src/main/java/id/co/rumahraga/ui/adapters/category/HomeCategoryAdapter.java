package id.co.rumahraga.ui.adapters.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import id.co.rumahraga.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import id.co.rumahraga.model.CategoryModel;
import id.co.rumahraga.model.listener.ItemClickListener;

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
        holder.tvName.setText(categoryModels.get(holder.getBindingAdapterPosition()).getName());
        Glide.with(context).load(categoryModels.get(holder.getBindingAdapterPosition()).getImage())
                .override(300, 300).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                .into(holder.ivCategory);

    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategories);
            ivCategory = itemView.findViewById(R.id.ivCategory);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("category", getBindingAdapterPosition(), categoryModels.get(getBindingAdapterPosition()));
                }
            });

            
        }
    }
}
