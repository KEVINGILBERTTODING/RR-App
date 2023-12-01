package id.co.rumahraga.ui.adapters.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.co.rumahraga.R;

import id.co.rumahraga.model.NotificationModel;
import id.co.rumahraga.model.listener.ItemClickListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VH> {
    private Context context;
    private List<NotificationModel> notificationModelList;
    private ItemClickListener itemClickListener;

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }


    public void setItemClickListener(ItemClickListener itemClickListener)  {
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public NotificationAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_notification, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.VH holder, int position) {
        if (notificationModelList.get(holder.getBindingAdapterPosition()).getType() == 1) { // success
            holder.tvTitle.setText("Pembayaran berhasil");
            holder.tvContent.setText(context.getString(R.string.notif_success));
            holder.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_success));
            holder.tvTitle.setTextColor(context.getColor(R.color.main));

        }else if (notificationModelList.get(holder.getBindingAdapterPosition()).getType() == 0) {
            holder.tvTitle.setText("Pembayaran gagal");
            holder.tvContent.setText(context.getString(R.string.notif_failed));
            holder.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_failed));
            holder.tvTitle.setTextColor(context.getColor(R.color.red));


        }

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent;
        private ImageView ivStatus;
        public VH(@NonNull View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tvContent);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivStatus = itemView.findViewById(R.id.ivStatus);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener("notification", getBindingAdapterPosition(), notificationModelList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
