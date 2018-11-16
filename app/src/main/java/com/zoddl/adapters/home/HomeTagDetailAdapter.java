package com.zoddl.adapters.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoddl.model.home.HomeCommonData;
import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.AdapterTagDetailBinding;
import com.zoddl.utils.AppConstant;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by Abhishek Tiwari on 4/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class HomeTagDetailAdapter extends RecyclerView.Adapter<HomeTagDetailAdapter.ViewHolder> {
    private Context context;
    private List<HomeCommonData> payloaditemarraylist;
    public OnItemClickListener onItemClickListener;
    JSONObject mReports;

    public HomeTagDetailAdapter(Context context,List<HomeCommonData> payloaditemarraylist,JSONObject mReports) {
        this.context = context;
        this.payloaditemarraylist = payloaditemarraylist;
        this.mReports = mReports;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_tag_detail, viewGroup, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (position){
            case 0:
                holder.txt_tag_name.setText("Cash+"+ " ("+payloaditemarraylist.get(0).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(0).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity)context).callHomeSortFragment("cash+");
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS);

                    }
                });
                break;
            case 1:
                holder.txt_tag_name.setText("Cash-"+ " ("+payloaditemarraylist.get(1).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(1).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity)context).callHomeSortFragment("cash-");
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS);
                    }
                });
                break;
            case 2:
                holder.txt_tag_name.setText("Bank+"+ " ("+payloaditemarraylist.get(2).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(2).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity)context).callHomeSortFragment("bank+");
                         ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS);
                    }
                });
                break;
            case 3:
                holder.txt_tag_name.setText("Bank-"+ " ("+payloaditemarraylist.get(3).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(3).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity)context).callHomeSortFragment("bank-");
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS);
                    }
                });
                break;
            case 4:
                holder.txt_tag_name.setText("Other+"+ " ("+payloaditemarraylist.get(4).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(4).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity)context).callHomeSortFragment("other");
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS);
                    }
                });
                break;
            case 5:
                holder.txt_tag_name.setText("Gallery"+ " ("+payloaditemarraylist.get(5).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(5).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY);
                    }
                });

                break;
            case 6:
                holder.txt_tag_name.setText("Document"+ " ("+payloaditemarraylist.get(6).getImages().size()+")");
                holder.txt_price.setText("Rs " +payloaditemarraylist.get(6).getTotal());
                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_DOC);
                    }
                });
                break;
            case 7:
                holder.tv_see_all.setVisibility(View.GONE);
                holder.txt_tag_name.setText("Report");
                holder.txt_price.setVisibility(View.GONE);
                break;
        }

        holder.binding.rvSubGallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        if (position == 7){
            holder.binding.rvSubGallery.setAdapter(new HomeSubMainReportAdapter(context,mReports));
        }else{
            holder.binding.rvSubGallery.setAdapter(new HomeSubMainAdapter(context,payloaditemarraylist.get(position).getImages()));
        }


        holder.binding.rvSubGallery.setNestedScrollingEnabled(false);
        holder.binding.rvSubGallery.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));


    }



    @Override
    public int getItemCount() {
        return payloaditemarraylist.size()+1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterTagDetailBinding binding;
        private TextView txt_tag_name;
        private TextView txt_price;
        private TextView tv_see_all;
        private LinearLayout ln_home_data;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            txt_tag_name = (TextView) itemView.findViewById(R.id.tv_tag_name);
            txt_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_see_all = (TextView) itemView.findViewById(R.id.tv_see_all);
            ln_home_data = (LinearLayout) itemView.findViewById(R.id.ln_home_data);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }

}
    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
