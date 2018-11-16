package com.zoddl.adapters.report;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.databinding.AdapterReportTagBinding;
import com.zoddl.interfaces.OnEventListener;
import com.zoddl.model.report.ReportSecondaryTag;

import java.util.ArrayList;

import static com.zoddl.interfaces.StringConstant._DOCUMENT_GALLERY;

/**
 * Created by Abhishek Tiwari on 10/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class ReportTagAdapter2 extends RecyclerView.Adapter<ReportTagAdapter2.ViewHolder> {

    private Context context;

    ArrayList<ReportSecondaryTag> mSecondarytagtype;
    OnEventListener mOnEventListenerTollbar;
    public void setOnEventListenerTollbar(OnEventListener mOnEventListenerTollbar){
        this.mOnEventListenerTollbar = mOnEventListenerTollbar;
    }
    public ReportTagAdapter2(Context context, ArrayList<ReportSecondaryTag> mSecondarytagtype) {
        this.context = context;
        this.mSecondarytagtype = mSecondarytagtype;
    }

    @Override
    public ReportTagAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_report_tag, parent, false);
        return new ReportTagAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportTagAdapter2.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final ReportSecondaryTag model = mSecondarytagtype.get(position);
        holder.binding.tvTagName.setText(mSecondarytagtype.get(position).getSecondaryName());

        try {
            if (mSecondarytagtype.get(position).getSourceType().equalsIgnoreCase(_DOCUMENT_GALLERY)){
                holder.binding.tvTagName.setBackground(context.getResources().getDrawable(R.drawable.button_tag_gray));
            }
        } catch (Exception mE) {
            mE.printStackTrace();
        }

        if (model.isSelected()) {
            holder.binding.ivTick.setVisibility(View.VISIBLE);
        }

        holder.binding.tvTagName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectedPosition = holder.getAdapterPosition();
               // notifyDataSetChanged();
                //selectedPosition = holder.getAdapterPosition();
                if (mOnEventListenerTollbar!=null){
                    mOnEventListenerTollbar.onEvent(position,holder.binding.ivTick);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSecondarytagtype.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterReportTagBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

