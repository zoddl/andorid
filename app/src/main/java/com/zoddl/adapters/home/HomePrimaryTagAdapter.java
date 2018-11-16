package com.zoddl.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.model.home.HomePrimaryTagPayload;

import java.util.List;


/**
 * Created by mobiloitte on 4/7/17.
 */

public class HomePrimaryTagAdapter extends RecyclerView.Adapter<HomePrimaryTagAdapter.ViewHolder> {

    private Context context;
    private List<HomePrimaryTagPayload> primarytagtype;
    public OnItemClickListener onItemClickListener;

    public HomePrimaryTagAdapter(Context context,List<HomePrimaryTagPayload> primarytagtype) {
        this.context = context;
        this.primarytagtype = primarytagtype;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
     holder.textView_primary_name.setText(primarytagtype.get(position).getPrime_Name());
    }

    @Override
    public int getItemCount() {
        return primarytagtype.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {



        private TextView textView_primary_name;
        //private AdapterTagBinding tagHomeBinding;

        public ViewHolder(View itemView) {
            super(itemView);
          // tagHomeBinding = DataBindingUtil.bind(itemView);
            textView_primary_name = (TextView)itemView.findViewById(R.id.tv_tag_name);
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