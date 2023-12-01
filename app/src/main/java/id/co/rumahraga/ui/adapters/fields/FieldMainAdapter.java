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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FieldMainAdapter extends RecyclerView.Adapter<FieldMainAdapter.VH> {
    private Context context;
    private List<FieldModel> fieldModelList;
    private ItemClickListener itemClickListener;
    private List<String> ratinglist;


    public FieldMainAdapter(Context context, List<FieldModel> fieldModelList) {
        this.context = context;
        this.fieldModelList = fieldModelList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public FieldMainAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_field_main, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldMainAdapter.VH holder, int position) {
        holder.tvFieldName.setText(fieldModelList.get(holder.getBindingAdapterPosition()).getName());
        holder.tvLocation.setText(fieldModelList.get(holder.getBindingAdapterPosition()).getCity_name());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        holder.tvPrice.setText("Rp. " + decimalFormat.format(Double.parseDouble(String.valueOf(fieldModelList.get(holder.getBindingAdapterPosition()).getPrice()))));
        Glide.with(context).load(fieldModelList.get(holder.getBindingAdapterPosition()).getImage())
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200, 200)
                .into(holder.ivField);

        holder.tvRating.setText(fieldModelList.get(holder.getBindingAdapterPosition()).getRating());



    }

    @Override
    public int getItemCount() {
        return fieldModelList.size();
    }

    public void filter(ArrayList<FieldModel> filteredList) {
        this.fieldModelList = filteredList;
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvFieldName, tvLocation, tvRating, tvPrice;
        private ImageView ivField, ivRating;
        public VH(@NonNull View itemView) {
            super(itemView);

            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivField = itemView.findViewById(R.id.ivField);
            ivRating = itemView.findViewById(R.id.ivRating);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("field", getBindingAdapterPosition(), fieldModelList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
