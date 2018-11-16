package com.zoddl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.databinding.RowCurrentPlanItemBinding;

/**
 * Created by mobiloitte on 6/7/17.
 */

public class CurrentPlanAdapter extends RecyclerView.Adapter<CurrentPlanAdapter.ViewHolder> {
    private Context context;
    private int position = 0;

    public CurrentPlanAdapter(Context context) {
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_current_plan_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        if (position == i) {
            holder.binding.cbPlan.setChecked(true);
        } else {
            holder.binding.cbPlan.setChecked(false);
        }

        holder.binding.cbPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowCurrentPlanItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
