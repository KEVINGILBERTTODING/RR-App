package id.co.rumahraga.ui.adapters.event;

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
import id.co.rumahraga.model.EventModel;
import id.co.rumahraga.model.listener.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.VH> {
    private Context context;
    private List<EventModel> eventModelList;
    private ItemClickListener itemClickListener;

    public EventAdapter(Context context, List<EventModel> eventModelList) {
        this.context = context;
        this.eventModelList = eventModelList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EventAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_event, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.VH holder, int position) {
        holder.tvEventCategory.setText(eventModelList.get(holder.getBindingAdapterPosition()).getEvent_category());
        holder.tvEventName.setText(eventModelList.get(holder.getBindingAdapterPosition()).getTitle());
        holder.tvEventDate.setText(eventModelList.get(holder.getBindingAdapterPosition()).getDate());

        Glide.with(context).load(eventModelList.get(holder.getBindingAdapterPosition()).getPicture_name())
                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).into(holder.ivEvent);


    }

    public void filter(ArrayList<EventModel> filteredList) {
        this.eventModelList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private ImageView ivEvent;
        private TextView tvEventName, tvEventCategory, tvEventDate;
        public VH(@NonNull View itemView) {
            super(itemView);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            tvEventCategory = itemView.findViewById(R.id.tvEventCategories);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("event", getBindingAdapterPosition(), eventModelList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
