package com.zoddl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

import java.util.List;

/**
 * Created by mobiloitte on 10/7/17.
 */

public class PostTagSubMainAdapter extends RecyclerView.Adapter<PostTagSubMainAdapter.ViewHolder> implements StringConstant{
    private Context context;
    List<Image> images;

    public PostTagSubMainAdapter(Context context,List<Image> listImages) {
        this.context = context;
        this.images = listImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_sub_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int possition) {

        try {

            String newFormattedDate = CommonUtils.checkRangeAndFindFormattedDate(images.get(possition).getTagSendDate());
            holder.binding.tvDate.setText(newFormattedDate);

            holder.binding.tvCash.setText(images.get(possition).getTagType());
            holder.binding.tvAmount.setText("Rs."+images.get(possition).getAmount());

           /* if (images.get(possition).getTagType().equalsIgnoreCase(_OTHER)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamBlue));
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamYellow));
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamGreen));
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorCamRed));
            }*/

            if (images.get(possition).getTagType().equalsIgnoreCase(_OTHER)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagOther));
                holder.binding.tvCash.setText("O");
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCPlus));
                holder.binding.tvCash.setText("C+");
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCMinus));
                holder.binding.tvCash.setText("C-");
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBPlus));
                holder.binding.tvCash.setText("B+");
            } else if (images.get(possition).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                holder.binding.tvCash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBMinus));
                holder.binding.tvCash.setText("B-");
            }

            String subTag = "";
            for (int k = 0; k < images.get(possition).getSecondaryTag().size(); k++) {
                SecondaryTag item = images.get(possition).getSecondaryTag().get(k);
                if (k == 0)
                    subTag = subTag.concat(item.getSecondaryName());
                else
                    subTag = subTag.concat("," + item.getSecondaryName());
            }
            holder.binding.tvTag.setText(subTag);

            Glide.with(context)
                    .load(images.get(possition).getImageUrlThumb())
                    .asBitmap()
                    .placeholder(R.drawable.ic_dummy)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target,
                                                   boolean isFirstResource) {
                            holder.binding.progressBar.setVisibility(View.GONE);

                            String docEx = DocUtils.findDocExe(images.get(possition).getImageUrl());
                            if (docEx.endsWith("jpg")||docEx.endsWith("jpeg")||docEx.endsWith("png")){

                            }else if (docEx.endsWith("pdf")){
                                holder.binding.adapterDocWebview.setVisibility(View.VISIBLE);
                                holder.binding.adapterDocWebview.fromUrl(images.get(possition).getImageUrl())
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
                    .into(new SimpleTarget<Bitmap>(150,105) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            holder.binding.progressBar.setVisibility(View.GONE);
                            holder.binding.ivItem.setImageBitmap(resource);
                        }
                    });



            int width = CommonUtils.getDisplayWidth(context) / 3;
            holder.binding.rlParent.setLayoutParams(new RelativeLayout.LayoutParams(width - 12, ViewGroup.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width - 12, (int) (width * 0.60));
            holder.binding.cvItem.setLayoutParams(lp);

            holder.binding.ivItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).getImagesData(images.get(possition).getId());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
                    //((HomeActivity) context).getImagesData(images.get(possition));
                    //((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);

                }
            });

            holder.binding.adapterDocWebview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).getImagesData(images.get(possition).getId());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
                    //((HomeActivity) context).getImagesData(images.get(possition));
                    //((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterTagSubDetailBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
