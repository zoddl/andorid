package com.zoddl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.AdapterTagDetailBinding;
import com.zoddl.model.postdetials.Payload;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.paging.PaginationAdapterCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.zoddl.utils.CommonUtils.getMonthName;

/**
 * Created by Abhishek Tiwari on 13/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class HomePostSortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Payload> data;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    static PaginationAdapterCallback mCallback;

    public HomePostSortAdapter(Context context) {
        this.context = context;
        this.data= new ArrayList<>();
    }

    public static void setOnEventListener(PaginationAdapterCallback listener) {
        mCallback = listener;
    }

    private boolean flag = false;
    public HomePostSortAdapter(Context context,boolean flag) {
        this.context = context;
        this.data = new ArrayList<>();
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (i) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.adapter_tag_detail, viewGroup, false);
                viewHolder = new ViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int i) {

        switch (getItemViewType(i)) {

            case ITEM:
                final ViewHolder holder = (ViewHolder) mHolder;

                String month = getMonthName(data.get(i).getDate());
                boolean recent = getCurrent(month);
                if (recent){
                    holder.binding.tvTagName.setText("Recent"+" ("+data.get(i).getCount()+")");
                }else{
                    holder.binding.tvTagName.setText(month+" ("+data.get(i).getCount()+")");
                }

                holder.binding.tvPrice.setText("Rs."+data.get(i).getTotal());


                PostTagSubMainAdapter untagedSubMainAdapter = new PostTagSubMainAdapter(context,data.get(i).getImages());
                holder.binding.rvSubGallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.binding.rvSubGallery.setAdapter(untagedSubMainAdapter);
                holder.binding.tvTagName.setText(holder.binding.tvTagName.getText().toString());

                if (flag){
                    holder.binding.tvSeeAll.setVisibility(View.GONE);
                }

                holder.binding.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((HomeActivity)context).callPostSortFragment(data.get(i));
                        ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH_DETAILS);
                    }
                });

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) mHolder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }


    }

    private boolean getCurrent(String mMonth) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        String value = getMonthName(formattedDate);
        if (mMonth.equalsIgnoreCase(value)){
            return true;
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterTagDetailBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == data.size()-1 && isLoadingAdded) ? LOADING : ITEM;
    }
    public void add(Payload r) {
        data.add(r);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<Payload> moveResults) {
        for (Payload result : moveResults) {
            add(result);
        }
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Payload());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = data.size() - 1;
        Payload result = getItem(position);

        if (result != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Payload getItem(int position) {
        return data.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(data.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }
}


