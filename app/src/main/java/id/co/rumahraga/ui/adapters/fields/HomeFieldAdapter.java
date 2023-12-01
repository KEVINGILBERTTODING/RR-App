package id.co.rumahraga.ui.adapters.fields;

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
import id.co.rumahraga.R;

import id.co.rumahraga.model.FieldModel;
import id.co.rumahraga.model.listener.ItemClickListener;

import java.util.List;

public class HomeFieldAdapter extends RecyclerView.Adapter<HomeFieldAdapter.ViewHolder> {
    private Context context;
    private List<FieldModel> fieldModelList;
    private ItemClickListener itemClickListener;

    public HomeFieldAdapter(Context context, List<FieldModel> fieldModelList) {
        this.context = context;
        this.fieldModelList = fieldModelList;
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HomeFieldAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_field_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFieldAdapter.ViewHolder holder, int position) {
        holder.tvFieldName.setText(fieldModelList.get(holder.getBindingAdapterPosition()).getName());
        Glide.with(context).load(fieldModelList.get(holder.getBindingAdapterPosition()).getImage())
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        if (fieldModelList != null && fieldModelList.size() > 5) {
            return 5;
        }else {
            return fieldModelList.size();

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvFieldName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            ivImage = itemView.findViewById(R.id.ivField);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("field", getBindingAdapterPosition(), fieldModelList.get(getBindingAdapterPosition()));
                }
            });

        }
    }
}
