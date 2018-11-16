package com.zoddl.adapters.hometagslist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zoddl.model.home.HomeDetailTagWise.Image;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DocUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;
import java.util.List;

/**
 * Created by mobiloitte on 4/7/17.
 */

public class HomeSeeAllSubMainAdapter extends RecyclerView.Adapter<HomeSeeAllSubMainAdapter.ViewHolder> implements StringConstant, OnPageChangeListener, OnLoadCompleteListener{
    private Context context;
    private List<Image> images;

    public HomeSeeAllSubMainAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_sub_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        try {

            String newFormattedDate = CommonUtils.checkRangeAndFindFormattedDate(images.get(i).getTagSendDate());
            holder.txt_date.setText(newFormattedDate);

            holder.txt_cash.setText(images.get(i).getTagType());
            holder.txt_amount.setText("Rs."+images.get(i).getAmount());

            /*if (images.get(i).getTagType().equalsIgnoreCase(_OTHER)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            } else if (images.get(i).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorCamBlue));
            } else if (images.get(i).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorCamYellow));
            } else if (images.get(i).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorCamGreen));
            } else if (images.get(i).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorCamRed));
            }*/

            if (images.get(i).getTagType().equalsIgnoreCase(_OTHER)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagOther));
                holder.txt_cash.setText("O");
            } else if (images.get(i).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCPlus));
                holder.txt_cash.setText("C+");
            } else if (images.get(i).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagCMinus));
                holder.txt_cash.setText("C-");
            } else if (images.get(i).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBPlus));
                holder.txt_cash.setText("B+");
            } else if (images.get(i).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                holder.txt_cash.setBackgroundColor(context.getResources().getColor(R.color.colorTagBMinus));
                holder.txt_cash.setText("B-");
            }


            String subTag = "";
            for (int k = 0; k < images.get(i).getSecondaryTag().size(); k++) {
                com.zoddl.model.home.HomeDetailTagWise.SecondaryTag item = images.get(i).getSecondaryTag().get(k);
                if (k == 0)
                    subTag = subTag.concat(item.getSecondaryName());
                else
                    subTag = subTag.concat("," + item.getSecondaryName());
            }

            if (subTag.length()!=0){
                if (images.get(i).getDescription().length()!=0){
                    holder.txt_tag.setText(subTag+","+images.get(i).getDescription());
                }else{
                    holder.txt_tag.setText(","+subTag);
                }
            }


            final String imageUrl = images.get(i).getImageUrlThumb();



            if (images.get(i).getIsUploaded().equalsIgnoreCase("0")) {
                holder.img_yellow.setBackgroundResource(R.drawable.button_camera_red);

                File file = new File(imageUrl);
                Uri imageUri = Uri.fromFile(file);

                Glide.with(context)
                        .load(imageUri)
                        .asBitmap()
                        .placeholder(R.drawable.ic_dummy)
                        .listener(new RequestListener<Uri, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<Bitmap> target,
                                    boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.img_item.setImageResource(R.drawable.icon11);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target,
                                    boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>(150,105) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.img_item.setImageBitmap(resource);
                            }
                        });
            }else {
                holder.img_yellow.setBackgroundResource(R.drawable.button_camera_green);
                Glide.with(context)
                        .load(imageUrl)
                        .asBitmap()
                        .error(R.drawable.document)
                        .placeholder(R.drawable.ic_dummy)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target,
                                    boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);

                                String docEx = DocUtils.findDocExe(imageUrl);
                                if (docEx.endsWith("jpg")||docEx.endsWith("jpeg")||docEx.endsWith("png")){

                                }else if (docEx.endsWith("pdf")){
                                    holder.doc_pdfView.setVisibility(View.VISIBLE);

                                    holder.doc_pdfView.fromUrl(imageUrl)
                                            .enableSwipe(true) // allows to block changing pages using
                                            .defaultPage(0)
                                            .swipeHorizontal(false)
                                            .enableAntialiasing(true)
                                            .swipeHorizontal(false)
                                            .enableAntialiasing(true)
                                            .loadFromUrl();

                                    holder.doc_pdfView.zoomTo(2);

                                }else{
                                    holder.img_item.setImageResource(R.drawable.icon11);
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
                                holder.progressBar.setVisibility(View.GONE);
                                holder.img_item.setImageBitmap(resource);
                            }
                        });

            }


            holder.doc_pdfView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).getImagesData(images.get(i).getId());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
                }
            });

            holder.img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).getImagesData(images.get(i).getId());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_DETAIL);
                }
            });

            int width = CommonUtils.getDisplayWidth(context) / 3;
            holder.binding.rlParent.setLayoutParams(new RelativeLayout.LayoutParams(width - 12, ViewGroup.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width - 12, (int) (width * 0.60));
            holder.binding.cvItem.setLayoutParams(lp);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        private AdapterTagSubDetailBinding binding;
        private ImageView img_item;
        private ImageView img_yellow;
        private PDFView doc_pdfView;
        private TextView txt_cash;
        private TextView txt_amount;
        private ImageView img_down_aarow;
        private TextView txt_date;
        private TextView txt_tag;


        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            img_item =  itemView.findViewById(R.id.iv_item);
            doc_pdfView = itemView.findViewById(R.id.adapter_doc_webview);
            img_yellow =  itemView.findViewById(R.id.img_yellow);
            txt_cash = itemView.findViewById(R.id.tv_cash);
            txt_amount = itemView.findViewById(R.id.tv_amount);
            img_down_aarow =  itemView.findViewById(R.id.iv_down_arrow);
            txt_date =  itemView.findViewById(R.id.tv_date);
            txt_tag =  itemView.findViewById(R.id.tv_tag);


        }
    }

}
