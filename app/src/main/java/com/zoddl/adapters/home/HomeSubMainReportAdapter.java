package com.zoddl.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeSubMainReportAdapter extends RecyclerView.Adapter<HomeSubMainReportAdapter.ViewHolder> {
    private Context context;

    private JSONObject list;
    private JSONArray mJSONArray;
    public OnItemClickListener onItemClickListener;

    public HomeSubMainReportAdapter(Context context,JSONObject list) {
        this.context = context;
        this.list = list;
        try {
            mJSONArray = this.list.getJSONArray("Images");
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowhomesubreport, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.iv_down_arrow_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject reportJson = mJSONArray.getJSONObject(position);
                    ((HomeActivity) context).callReportFragment(reportJson.toString());
                    ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_WEB);
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }

            }
        });

        try {
            JSONObject reportJson = mJSONArray.getJSONObject(position);
            JSONArray rJsonArray = reportJson.getJSONArray("reportjson");

            JSONObject rJsonArrayObj = rJsonArray.getJSONObject(0);

                Iterator<String> keys = rJsonArrayObj.keys();
                StringBuilder sb = new StringBuilder();
                while( keys.hasNext())
                {
                    String key = keys.next();
                    JSONObject innerJObject = rJsonArrayObj.getJSONObject(key);
                    sb.append(innerJObject.get("Prime_Name"));
                    sb.append("\n");
                }
            holder.tv_tag_name_home.setText(sb.toString());

        } catch (JSONException mE) {
            mE.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return mJSONArray.length() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView tv_tag_name_home;
        private ImageView iv_down_arrow_home;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_tag_name_home = (TextView)itemView.findViewById(R.id.tv_tag_name);
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

