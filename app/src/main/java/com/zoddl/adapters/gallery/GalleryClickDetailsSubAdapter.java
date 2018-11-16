package com.zoddl.adapters.gallery;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel.ImagesItem;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DocUtils;

import java.util.List;

/**
 * Created by garuv on 9/5/18.
 */

public class GalleryClickDetailsSubAdapter extends RecyclerView.Adapter<GalleryClickDetailsSubAdapter.ViewHolder> implements StringConstant {

    private Context context;
    private List<ImagesItem> imagesItems;


    public GalleryClickDetailsSubAdapter(Context context, List<ImagesItem> imagesItems) {
        this.context = context;
        this.imagesItems = imagesItems;
    }


    @Override
    public GalleryClickDetailsSubAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_sub_detail, viewGroup, false);
        return new GalleryClickDetailsSubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryClickDetailsSubAdapter.ViewHolder holder, final int i) {

        try {

            String newFormattedDate = CommonUtils.checkRangeAndFindFormattedDate(imagesItems.get(i).getTagSendDate());
            holder.txt_date.setText(newFormattedDate);

            holder.txt_cash.setText(imagesItems.get(i).getTagType());
            holder.txt_amount.setText("Rs."+imagesItems.get(i).getAmount());

            if (imagesItems.get(i).getTagType().equalsIgnoreCase(_OTHER)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagOther));
                holder.txt_cash.setText("O");
            } else if (imagesItems.get(i).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCPlus));
                holder.txt_cash.setText("C+");
            } else if (imagesItems.get(i).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCMinus));
                holder.txt_cash.setText("C-");
            } else if (imagesItems.get(i).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBPlus));
                holder.txt_cash.setText("B+");
            } else if (imagesItems.get(i).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBMinus));
                holder.txt_cash.setText("B-");
            }

            Glide.with(context)
                    .load(imagesItems.get(i).getImageUrlThumb())
                    .asBitmap()
                    .placeholder(R.drawable.ic_dummy)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target,
                                                   boolean isFirstResource) {
                            holder.binding.progressBar.setVisibility(View.GONE);

                            String docEx = DocUtils.findDocExe(imagesItems.get(i).getImageUrl());
                            if (docEx.endsWith("jpg")||docEx.endsWith("jpeg")||docEx.endsWith("png")){

                            }else if (docEx.endsWith("pdf")){
                                holder.binding.adapterDocWebview.setVisibility(View.VISIBLE);
                                holder.binding.adapterDocWebview.fromUrl(imagesItems.get(i).getImageUrl())
                                        .enableSwipe(true) // allows to block changing pages using swipe
                                        .defaultPage(0)
                                        .swipeHorizontal(false)
                                        .enableAntialiasing(true)
                                        .swipeHorizontal(false)
                                        .enableAntialiasing(true)
                                        .loadFromUrl();

                                holder.binding.adapterDocWebview.zoomTo(2);
                            }else{
                                holder.binding.ivItem.setImageResource(R.drawable.icon11);
                            }

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<Bitmap>(150, 105) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.img_item.setImageBitmap(resource);
                        }
                    });


            String subTag = "";
            for (int k = 0; k < imagesItems.get(i).getSecondaryTag().size(); k++) {
                SecondaryTag item = imagesItems.get(i).getSecondaryTag().get(k);
                if (k == 0)
                    subTag = subTag.concat(item.getSecondaryName());
                else
                    subTag = subTag.concat("," + item.getSecondaryName());
            }
            holder.txt_tag.setText(subTag);

            int width = CommonUtils.getDisplayWidth(context) / 3;
            holder.binding.rlParent.setLayoutParams(new RelativeLayout.LayoutParams(width - 12, ViewGroup.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width - 12, (int) (width * 0.60));
            holder.binding.cvItem.setLayoutParams(lp);


            holder.binding.adapterDocWebview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).getImagesData(imagesItems.get(i).getId());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
                }
            });

            holder.layoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).getImagesData(imagesItems.get(i).getId());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imagesItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterTagSubDetailBinding binding;
        private ProgressBar progressBar;
        private ImageView img_item;
        private TextView txt_cash;
        private TextView txt_amount;
        private TextView txt_date;
        private TextView txt_tag;
        private FrameLayout layoutView;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            img_item = (ImageView) itemView.findViewById(R.id.iv_item);
            txt_cash = (TextView) itemView.findViewById(R.id.tv_cash);
            txt_amount = itemView.findViewById(R.id.tv_amount);
            txt_date = (TextView) itemView.findViewById(R.id.tv_date);
            txt_tag = (TextView) itemView.findViewById(R.id.tv_tag);
            layoutView = itemView.findViewById(R.id.cv_item);

        }
    }

}
