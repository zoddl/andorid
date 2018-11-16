package com.zoddl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.AdapterTagSubDetailBinding;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.postdetials.Image;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DocUtils;
import com.zoddl.utils.paging.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class PostSubMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StringConstant{
    private Context context;
    private List<Image> images;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    static PaginationAdapterCallback mCallback;

    public PostSubMainAdapter(Context context) {
        this.context = context;
        this.images= new ArrayList<>();
    }

    public static void setOnEventListener(PaginationAdapterCallback listener) {
        mCallback = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_sub_detail, viewGroup, false);
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.adapter_tag_sub_detail, viewGroup, false);
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {

            case ITEM:
                try {
                    final ViewHolder mHolder = (ViewHolder) holder;


                    String newFormattedDate = CommonUtils.checkRangeAndFindFormattedDate(images.get(position).getTagSendDate());
                    mHolder.binding.tvDate.setText(newFormattedDate);

                    mHolder.binding.tvCash.setText(images.get(position).getTagType());
                    mHolder.binding.tvAmount.setText("Rs."+images.get(position).getAmount());

                    /*if (images.get(position).getTagType().equalsIgnoreCase(_OTHER)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamBlue));
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamYellow));
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamGreen));
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamRed));
                    }*/

                    if (images.get(position).getTagType().equalsIgnoreCase(_OTHER)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagOther));
                        mHolder.binding.tvCash.setText("O");
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCPlus));
                        mHolder.binding.tvCash.setText("C+");
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCMinus));
                        mHolder.binding.tvCash.setText("C-");
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBPlus));
                        mHolder.binding.tvCash.setText("B+");
                    } else if (images.get(position).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                        mHolder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBMinus));
                        mHolder.binding.tvCash.setText("B-");
                    }


                    String subTag = "";
                    for (int k = 0; k < images.get(position).getSecondaryTag().size(); k++) {
                        SecondaryTag item = images.get(position).getSecondaryTag().get(k);
                        if (k == 0)
                            subTag = subTag.concat(item.getSecondaryName());
                        else
                            subTag = subTag.concat("," + item.getSecondaryName());
                    }
                    mHolder.binding.tvTag.setText(subTag);

                    Glide.with(context)
                            .load(images.get(position).getImageUrl())
                            .asBitmap()
                            .placeholder(R.drawable.ic_dummy)

                            .listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<Bitmap> target,
                                                           boolean isFirstResource) {
                                    mHolder.binding.progressBar.setVisibility(View.GONE);

                                    String docEx = DocUtils.findDocExe(images.get(position).getImageUrl());
                                    if (docEx.endsWith("jpg")||docEx.endsWith("jpeg")||docEx.endsWith("png")){

                                    }else if (docEx.endsWith("pdf")){
                                        mHolder.binding.adapterDocWebview.setVisibility(View.VISIBLE);
                                        mHolder.binding.adapterDocWebview.fromUrl(images.get(position).getImageUrl())
                                                .enableSwipe(true) // allows to block changing pages using swipe
                                                .defaultPage(0)
                                                .swipeHorizontal(false)
                                                .enableAntialiasing(true)
                                                .swipeHorizontal(false)
                                                .enableAntialiasing(true)
                                                .loadFromUrl();

                                        mHolder.binding.adapterDocWebview.zoomTo(2);
                                    }else{
                                        mHolder.binding.ivItem.setImageResource(R.drawable.icon11);
                                    }

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
                                                               boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(new SimpleTarget<Bitmap>(150,105) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    mHolder.binding.progressBar.setVisibility(View.GONE);
                                    mHolder.binding.ivItem.setImageBitmap(resource);
                                }
                            });



                    int width = CommonUtils.getDisplayWidth(context) / 3;
                    mHolder.binding.rlParent.setLayoutParams(new RelativeLayout.LayoutParams(width - 12, ViewGroup.LayoutParams.WRAP_CONTENT));
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width - 12, (int) (width * 0.60));
                    mHolder.binding.cvItem.setLayoutParams(lp);

                    mHolder.binding.ivItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomeActivity) context).getImagesData(images.get(position).getId());
                            ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
               /* PostDetailFragment detailFragment=PostDetailFragment.newInstance(taglistItems.get(position));
                ((HomeActivity) context).setFragment(detailFragment);*/
                        }
                    });

                    mHolder.binding.adapterDocWebview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomeActivity) context).getImagesData(images.get(position).getId());
                            ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
               /* PostDetailFragment detailFragment=PostDetailFragment.newInstance(taglistItems.get(position));
                ((HomeActivity) context).setFragment(detailFragment);*/
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

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


    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == images.size()-1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterTagSubDetailBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void add(Image r) {
        images.add(r);
        notifyItemInserted(images.size() - 1);
    }

    public void addAll(List<Image> moveResults) {
        for (Image result : moveResults) {
            add(result);
        }
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Image());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = images.size() - 1;
        Image result = getItem(position);

        if (result != null) {
            images.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Image getItem(int position) {
        return images.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(images.size() - 1);

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
