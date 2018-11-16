package com.zoddl.adapters.gallery;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.AdapterTagDetailBinding;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel.PayloadItem;
import com.zoddl.utils.AppConstant;

import java.util.List;

/**
 * Created by garuv on 8/5/18.
 */
public class GalleryClickDetailsGallerytag extends RecyclerView.Adapter<GalleryClickDetailsGallerytag.MyViewHolder> implements StringConstant {

    private Context context;
    private List<PayloadItem> payloadItems1;
    private String mFromWhere;

    public GalleryClickDetailsGallerytag(Context context, String mFromWhere, List<PayloadItem> payloadItems1) {
        this.context = context;
        this.mFromWhere = mFromWhere;
        this.payloadItems1 = payloadItems1;
    }

    @Override
    public GalleryClickDetailsGallerytag.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_detail, viewGroup, false);
        return new GalleryClickDetailsGallerytag.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryClickDetailsGallerytag.MyViewHolder holder, final int position) {

        /*if (mFromWhere.equalsIgnoreCase(_PRIMARY)){
            holder.txt_tag_name.setText(payloadItems1.get(position).getSecondaryName()+ " ("+payloadItems1.get(position).getImages().size()+")");
        }else {*/
            holder.txt_tag_name.setText(payloadItems1.get(position).getPrimeName()+ " ("+payloadItems1.get(position).getImages().size()+")");
       // }

        holder.txt_price.setText("Rs" +payloadItems1.get(position).getTotal());
        holder.binding.rvSubGallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.binding.rvSubGallery.setAdapter(new GalleryClickDetailsSubAdapter(context,payloadItems1.get(position).getImages()));
        holder.binding.rvSubGallery.setNestedScrollingEnabled(false);
        holder.binding.rvSubGallery.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)context).callPostSortFragment(payloadItems1.get(position).getPrimeName()+ " ("+payloadItems1.get(position).getImages().size()+")",payloadItems1.get(position).getPrimaryTag(),payloadItems1.get(position).getSecondaryTag());

                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_PRIMARY_SECONDARY);
                //Toast.makeText(context,"That Is See All",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return payloadItems1.size();
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
