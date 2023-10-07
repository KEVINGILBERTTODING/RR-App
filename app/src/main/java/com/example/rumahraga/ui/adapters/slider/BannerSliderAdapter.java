package com.example.rumahraga.ui.adapters.slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.model.BannerModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class BannerSliderAdapter extends SliderViewAdapter<BannerSliderAdapter.SliderAdapterVH> {
    private Context context;
    private List<BannerModel> bannerModelList;

    public BannerSliderAdapter(Context context, List<BannerModel> bannerModelList) {
        this.context = context;
        this.bannerModelList = bannerModelList;
    }

    public void renewItems(List<BannerModel> sliderItems) {
        this.bannerModelList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.bannerModelList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(BannerModel sliderItem) {
        this.bannerModelList.add(sliderItem);
        notifyDataSetChanged();
    }


    @Override
    public BannerSliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_banner, parent, false);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(BannerSliderAdapter.SliderAdapterVH viewHolder, int position) {

        Glide.with(context).load(bannerModelList.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                .into(viewHolder.ivBanner);

    }

    @Override
    public int getCount() {
        return bannerModelList.size();
    }

    public class SliderAdapterVH extends ViewHolder {
        ImageView ivBanner;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            ivBanner = itemView.findViewById(R.id.ivBanner);
        }
    }
}
