package com.zoddl.navigationdrawer;
/**
 * Created by Abhishek Tiwari on 3/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.databinding.AdapterDrawerBinding;
import com.zoddl.utils.ValidationHelper;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private List<DrawerItem> data = new ArrayList<>();
    private Context context;

    public DrawerAdapter(Context context, List<DrawerItem> data) {
        this.context = context;
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_drawer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DrawerItem current = data.get(position);
        holder.drawerBinding.tvDrawerText.setText(ValidationHelper.checkNull(current.getTitle()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterDrawerBinding drawerBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            drawerBinding = DataBindingUtil.bind(itemView);
        }
    }
}
