package com.zoddl.adapters.gallery;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.AdapterTagDetailBinding;
import com.zoddl.model.gallery.TaglistItem;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.GalleryCustomFilter;

import java.util.List;

/**
 * Created by avanish on 8/5/18.
 */

public class GalleryMainAdapter extends RecyclerView.Adapter<GalleryMainAdapter.MyViewHolder> implements Filterable {
    private Context context;
    public List<TaglistItem> taglistItems;
    List<TaglistItem> taglistItemsSearch;
    GalleryCustomFilter filter;

    public GalleryMainAdapter(Context context, List<TaglistItem> taglistItems) {
        this.context = context;
        this.taglistItems = taglistItems;
        this.taglistItemsSearch = taglistItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_detail, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_tag_name.setText(taglistItems.get(position).getPrimeName()+ " ("+taglistItems.get(position).getImages().size()+")");

        holder.txt_price.setText("Rs " +taglistItems.get(position).getTotal());

        holder.binding.rvSubGallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.binding.rvSubGallery.setAdapter
                (new GallerySubMainAdapter(context,taglistItems.get(position).getImages()));
        holder.binding.rvSubGallery.setNestedScrollingEnabled(false);
        holder.binding.rvSubGallery.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!taglistItems.get(position).getPrimeName().equalsIgnoreCase("Uploading Data..")){

                ((HomeActivity)context).callPostSortFragment(taglistItems.get(position).getPrimeName(),taglistItems.get(position).getPrimaryTag());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_SORT);
                /*}else {
                    Toast.makeText(context, "Uploading.........", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return taglistItems.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null) {
            filter=new GalleryCustomFilter(taglistItemsSearch,this);
        }

        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterTagDetailBinding binding;
        private TextView txt_tag_name;
        private TextView txt_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            txt_tag_name = (TextView)itemView.findViewById(R.id.tv_tag_name);
            txt_price = (TextView)itemView.findViewById(R.id.tv_price);

        }
    }
}
