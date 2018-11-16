package com.zoddl.utils;

import android.widget.Filter;

import com.zoddl.adapters.gallery.GalleryMainAdapter;
import com.zoddl.model.gallery.TaglistItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 3/17/2016.
 */
public class GalleryCustomFilter extends Filter{

    GalleryMainAdapter adapter;
    List<TaglistItem> filterList;


    public GalleryCustomFilter(List<TaglistItem> filterList, GalleryMainAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<TaglistItem> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getPrimeName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

       adapter.taglistItems= (ArrayList<TaglistItem>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
