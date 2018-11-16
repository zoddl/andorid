package com.zoddl.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.model.home.ImagesItem;
//import com.zoddl.databinding.AdapterHomeSubTableDetailsBinding;

import java.util.List;

import static com.zoddl.interfaces.StringConstant._BANK_MINUS;
import static com.zoddl.interfaces.StringConstant._BANK_PLUS;
import static com.zoddl.interfaces.StringConstant._CASH_MINUS;
import static com.zoddl.interfaces.StringConstant._CASH_PLUS;
import static com.zoddl.interfaces.StringConstant._OTHER;


public class HomeSubMainAdapter extends RecyclerView.Adapter<HomeSubMainAdapter.ViewHolder> {
    private Context context;

    private List<ImagesItem> list;
    public OnItemClickListener onItemClickListener;

    public HomeSubMainAdapter(Context context,List<ImagesItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.homesubadapterlayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if (list.get(position).getType().equalsIgnoreCase("document")){
            holder.img_yellow_home.setImageResource(R.drawable.icon11);
        }

        holder.tv_cash_home.setText(list.get(position).getTagType());
        holder.tv_tag_name.setText(list.get(position).getPrimeName()+" ("+list.get(position).getCount()+")");
        holder.tv_tag_price.setText("Rs."+list.get(position).getAmount());

        try {
           /* if (list.get(position).getTagType().equalsIgnoreCase("Other")) {
                holder.tv_cash_home.setBackgroundColor(Color.parseColor("#585858"));
            } else if (list.get(position).getTagType().equalsIgnoreCase("Cash+")) {
                holder.tv_cash_home.setBackgroundColor(Color.parseColor("#61A4DE"));
            } else if (list.get(position).getTagType().equalsIgnoreCase("Cash-")) {
                holder.tv_cash_home.setBackgroundColor(Color.parseColor("#FAD162"));
            } else if (list.get(position).getTagType().equalsIgnoreCase("Bank+")) {
                holder.tv_cash_home.setBackgroundColor(Color.parseColor("#01AC4E"));
            } else if (list.get(position).getTagType().equalsIgnoreCase("Bank-")) {
                holder.tv_cash_home.setBackgroundColor(Color.parseColor("#FF4141"));
            }*/

            if (list.get(position).getTagType().equalsIgnoreCase(_OTHER)) {
                holder.tv_cash_home.setBackgroundColor(context.getResources().getColor(R.color.colorTagOther));
                holder.tv_cash_home.setText("O");
            } else if (list.get(position).getTagType().equalsIgnoreCase(_CASH_PLUS)) {
                holder.tv_cash_home.setBackgroundColor(context.getResources().getColor(R.color.colorTagCPlus));
                holder.tv_cash_home.setText("C+");
            } else if (list.get(position).getTagType().equalsIgnoreCase(_CASH_MINUS)) {
                holder.tv_cash_home.setBackgroundColor(context.getResources().getColor(R.color.colorTagCMinus));
                holder.tv_cash_home.setText("C-");
            } else if (list.get(position).getTagType().equalsIgnoreCase(_BANK_PLUS)) {
                holder.tv_cash_home.setBackgroundColor(context.getResources().getColor(R.color.colorTagBPlus));
                holder.tv_cash_home.setText("B+");
            } else if (list.get(position).getTagType().equalsIgnoreCase(_BANK_MINUS)) {
                holder.tv_cash_home.setBackgroundColor(context.getResources().getColor(R.color.colorTagBMinus));
                holder.tv_cash_home.setText("B-");
            }

        } catch (Exception mE) {
            mE.printStackTrace();
        }

       // if (list.get(position).get)

    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

       // private AdapterHomeSubTableDetailsBinding binding;

        private TextView tv_cash_home;
        private ImageView img_yellow_home;
        private TextView tv_tag_name;
        private TextView tv_tag_price;
        private ImageView iv_down_arrow_home;


        public ViewHolder(View itemView) {
            super(itemView);
           // binding = DataBindingUtil.bind(itemView);
            tv_cash_home = (TextView)itemView.findViewById(R.id.tv_cash);
            img_yellow_home = (ImageView)itemView.findViewById(R.id.img_yellow);
            tv_tag_name = (TextView)itemView.findViewById(R.id.tv_tag_name);
            tv_tag_price = (TextView)itemView.findViewById(R.id.tv_tag_price);
            iv_down_arrow_home = (ImageView) itemView.findViewById(R.id.iv_down_arrow);

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

