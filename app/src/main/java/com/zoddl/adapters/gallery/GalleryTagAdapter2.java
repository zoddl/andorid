package com.zoddl.adapters.gallery;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.databinding.AdapterTagBinding;
import com.zoddl.model.commommodel.SecondaryTag;

import java.util.List;

/**
 * Created by mobiloitte on 4/7/17.
 */

public class GalleryTagAdapter2 extends RecyclerView.Adapter<GalleryTagAdapter2.ViewHolder> {

    private Context context;
    private int selectedPosition = 0;
    private List<SecondaryTag> secondarytagtype;
    public  OnItemClickListener onItemClickListener;


    public GalleryTagAdapter2(Context context,List<SecondaryTag>secondarytagtype) {
        this.context = context;
        this.secondarytagtype = secondarytagtype;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txt_name.setText(secondarytagtype.get(position).getSecondaryName());
    }

    @Override
    public int getItemCount() {
        return secondarytagtype.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
    {

        private AdapterTagBinding tagHomeBinding;
        private TextView txt_name;


        public ViewHolder(View itemView) {
            super(itemView);
            tagHomeBinding = DataBindingUtil.bind(itemView);

            txt_name = (TextView)itemView.findViewById(R.id.tv_tag_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    public void setItemClickListener(GalleryTagAdapter2.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}