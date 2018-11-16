package com.zoddl.navigationdrawer;

/**
 * Created by Abhishek Tiwari on 3/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.databinding.FragmentDrawerBinding;

import java.util.ArrayList;
import java.util.List;

public class DrawerFragment extends Fragment {

    private Context context;
    private FragmentDrawerListener drawerListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public List<DrawerItem> getData() {
        String[] titles = context.getResources().getStringArray(R.array.drawer_array_text);
        List<DrawerItem> data = new ArrayList<>();
        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            DrawerItem navItem = new DrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout
        FragmentDrawerBinding drawerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drawer, container, false);

        DrawerAdapter adapter = new DrawerAdapter(getActivity(), getData());
        drawerBinding.rvDrawerList.setAdapter(adapter);
        drawerBinding.rvDrawerList.setLayoutManager(new LinearLayoutManager(getActivity()));

        drawerBinding.rvDrawerList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), drawerBinding.rvDrawerList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        drawerBinding.rvDrawerList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        return drawerBinding.getRoot();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
