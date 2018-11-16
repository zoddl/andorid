package com.zoddl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.databinding.AdapterNotifyItemsBinding;
import com.zoddl.fcmService.Message;

import java.util.List;

/**
 * Created by mobiloitte on 10/7/17.
 */

public class NotificationTagAdapter extends RecyclerView.Adapter<NotificationTagAdapter.ViewHolder> {

    private Context context;
    List<Message> items;

    public NotificationTagAdapter(Context context,List<Message> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public NotificationTagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_notify_items, parent, false);
        return new NotificationTagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationTagAdapter.ViewHolder holder, int position) {
        holder.mBinding.titleNotify.setText(items.get(position).getTitle());
        holder.mBinding.messageNotify.setText(items.get(position).getMessageId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterNotifyItemsBinding mBinding;
        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

        }
    }
}