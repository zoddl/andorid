package com.zoddl.adapters.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;

import com.zoddl.databinding.AdapterHomeTableDetailsBinding;
import com.zoddl.model.home.HomeDataModel.PayloadItem;

import java.util.List;

import static com.zoddl.interfaces.StringConstant._OTHER;
import static com.zoddl.interfaces.StringConstant._TOP_5_BANK_MINUS;
import static com.zoddl.interfaces.StringConstant._TOP_5_BANK_PLUS;
import static com.zoddl.interfaces.StringConstant._TOP_5_CASH_MINUS;
import static com.zoddl.interfaces.StringConstant._TOP_5_CASH_PLUS;

/**
 * Created by avanish on 11/5/18.
 */

public class TablehomeAdapter extends RecyclerView.Adapter<TablehomeAdapter.ViewHolder> {

    private Context context;
    private List<PayloadItem> payloaditemarraylist;
    private OnItemClickListener onItemClickListener;


    public TablehomeAdapter(Context context, List<PayloadItem> payloaditemarraylist) {
        this.context = context;
        this.payloaditemarraylist = payloaditemarraylist;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_table_details, parent, false);
        return new TablehomeAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull TablehomeAdapter.ViewHolder holder, int position) {

        if (payloaditemarraylist.get(position).getColumnHeading().equalsIgnoreCase("cash+")){
            holder.binding.columnHeading.setText(_TOP_5_CASH_PLUS);
        }else if (payloaditemarraylist.get(position).getColumnHeading().equalsIgnoreCase("cash-")){
            holder.binding.columnHeading.setText(_TOP_5_CASH_MINUS);

        }else if (payloaditemarraylist.get(position).getColumnHeading().equalsIgnoreCase("bank+")){
            holder.binding.columnHeading.setText(_TOP_5_BANK_PLUS);

        }else if (payloaditemarraylist.get(position).getColumnHeading().equalsIgnoreCase("bank-")){
            holder.binding.columnHeading.setText(_TOP_5_BANK_MINUS);
        }else if (payloaditemarraylist.get(position).getColumnHeading().equalsIgnoreCase("other")){
            holder.binding.columnHeading.setText(_OTHER);
        }else{
            holder.binding.columnHeading.setText("No.");
        }

        holder.binding.rvGallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.binding.rvGallery.setAdapter
                (new TablehomeSubAdapter(context,payloaditemarraylist.get(position).getCash()));
        holder.binding.rvGallery.setNestedScrollingEnabled(false);

        if(position%2==0){
            holder.binding.rvGalleryLayout.setBackgroundColor(Color.parseColor("#cbe8f5"));
        }else {
            holder.binding.rvGalleryLayout.setBackgroundColor(Color.parseColor("#abd7ed"));
        }



    }



    @Override
    public int getItemCount() {
        return payloaditemarraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterHomeTableDetailsBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
           // recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_table_row_data);


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
