package com.zoddl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.databinding.AdapterContactUsBinding;
import com.zoddl.model.ContactListModel;

import java.util.List;

/**
 * Created by Abhishek Tiwari on 5/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.ViewHolder> {

    private Context context;
    private List<ContactListModel> list;
    public ContactUsAdapter(Context context,List<ContactListModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_contact_us, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.tvMsgDate.setText(list.get(position).getDate());
        holder.binding.tvMsgText.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterContactUsBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
