package com.zoddl.adapters.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.databinding.AdapterHomeSubTableDetailsBinding;
import com.zoddl.R;
import com.zoddl.model.home.HomeDataModel.CashItem;

import java.util.List;

/**
 * Created by garuv on 11/5/18.
 */

public class TablehomeSubAdapter extends RecyclerView.Adapter<TablehomeSubAdapter.ViewHolder>  {

    private Context context;
    public List<CashItem> cashItemsitemarraylist;


    public  TablehomeSubAdapter(Context context, List<CashItem> cashItemsitemarraylist)
    {
        this.context = context;
        this.cashItemsitemarraylist = cashItemsitemarraylist;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_sub_table_details, parent, false);
        return new TablehomeSubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(cashItemsitemarraylist.get(position).getTotalAmount()==null ||
                cashItemsitemarraylist.get(position).getTotalAmount().length()<=0){
            holder.binding.txtTableSubData.setText("-");
        }else {
            holder.binding.txtTableSubData.setText(cashItemsitemarraylist.get(position).getTotalAmount());
        }
    }

    @Override
    public int getItemCount() {
        if(cashItemsitemarraylist!=null){
            return cashItemsitemarraylist.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterHomeSubTableDetailsBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
