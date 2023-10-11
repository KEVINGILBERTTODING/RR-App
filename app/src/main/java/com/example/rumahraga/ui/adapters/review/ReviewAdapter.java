package com.example.rumahraga.ui.adapters.review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.model.ReviewModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<ReviewModel> reviewModelList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.tvReview.setText(reviewModelList.get(holder.getBindingAdapterPosition()).getReview_text());
        holder.tvUsername.setText(reviewModelList.get(holder.getBindingAdapterPosition()).getUsername());
        holder.tvDate.setText(reviewModelList.get(holder.getBindingAdapterPosition()).getCreated_at());

        holder.ratingBar.setRating(reviewModelList.get(holder.getBindingAdapterPosition()).getStars());

        Glide.with(context).load(reviewModelList.get(holder.getBindingAdapterPosition()).getPhoto_profile())
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).override(100, 100)
                .into(holder.ivProfile);



    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvDate, tvReview;
        private ImageView ivProfile;
        private RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvReview = itemView.findViewById(R.id.tvTextReview);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
