package com.zoddl.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.model.commommodel.PrimaryTagTypeModel;

import java.util.ArrayList;

/**
 * Created by Abhishek Tiwari on 4/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class HomeTagAdapter extends RecyclerView.Adapter<HomeTagAdapter.ViewHolder> {

    private Context context;
    private int selectedPosition = 0;
    ArrayList<PrimaryTagTypeModel> primarytagreport;
    public OnItemClickListener onItemClickListener;


    public HomeTagAdapter(Context context,ArrayList<PrimaryTagTypeModel> primarytagreport) {
        this.context = context;
        this.primarytagreport = primarytagreport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.textView_report_primary_name.setText(primarytagreport.get(position).getprimaryName());
//        if (position == selectedPosition) {
//            holder.tagHomeBinding.tvTagName.setBackground(ContextCompat.getDrawable(context, R.drawable.button_tag_selected));
//        } else {
//            holder.tagHomeBinding.tvTagName.setBackground(ContextCompat.getDrawable(context, R.drawable.button_tag));
//        }
//
//        holder.tagHomeBinding.tvTagName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedPosition = holder.getAdapterPosition();
//                notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return primarytagreport.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        private TextView textView_report_primary_name;

        //private AdapterTagBinding tagHomeBinding;

        public ViewHolder(View itemView) {
            super(itemView);
           // tagHomeBinding = DataBindingUtil.bind(itemView);

            textView_report_primary_name = (TextView)itemView.findViewById(R.id.tv_tag_name);
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
