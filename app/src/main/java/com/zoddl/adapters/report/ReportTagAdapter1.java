package com.zoddl.adapters.report;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zoddl.R;
import com.zoddl.databinding.AdapterReportTagBinding;
import com.zoddl.interfaces.OnEventListener;
import com.zoddl.model.report.ReportPrimaryTag;

import java.util.ArrayList;

import static com.zoddl.interfaces.StringConstant._DOCUMENT_GALLERY;

/**
 * Created by avanish.info on 22/05/2018
 * for Apptology Pvt. Ltd.
 */
public class ReportTagAdapter1 extends RecyclerView.Adapter<ReportTagAdapter1.ViewHolder> {

    private Context context;
    ArrayList<ReportPrimaryTag> mPrimarytagtype;
    OnEventListener mOnEventListenerTollbar;
    public void setOnEventListenerTollbar(OnEventListener mOnEventListenerTollbar){
        this.mOnEventListenerTollbar = mOnEventListenerTollbar;
    }

    public ReportTagAdapter1(Context context, ArrayList<ReportPrimaryTag> mPrimarytagtype) {
        this.context = context;
        this.mPrimarytagtype = mPrimarytagtype;
    }

    @Override
    public ReportTagAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_report_tag, parent, false);
        return new ReportTagAdapter1.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportTagAdapter1.ViewHolder holder, final int position) {
        final ReportPrimaryTag model = mPrimarytagtype.get(position);
        holder.setIsRecyclable(false);
        holder.binding.tvTagName.setText(mPrimarytagtype.get(position).getPrimeName());
        try {
            if (mPrimarytagtype.get(position).getSourceType().equalsIgnoreCase(_DOCUMENT_GALLERY)){
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
                if (mOnEventListenerTollbar!=null){
                    mOnEventListenerTollbar.onEvent(position,holder.binding.ivTick);
                }
            }
        });

    }

    private void setItem(int pos, ImageView mIvTick) {

    }

    @Override
    public int getItemCount() {
        return mPrimarytagtype == null ? 0 :mPrimarytagtype.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterReportTagBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
