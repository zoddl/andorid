package com.zoddl.fragments;


import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoddl.R;
import com.zoddl.adapters.NotificationTagAdapter;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.databinding.FragmentAlertBinding;
import com.zoddl.fcmService.Message;
import com.zoddl.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import static com.zoddl.interfaces.StringConstant._MESSAGE;
import static com.zoddl.interfaces.StringConstant._SUCCESS;
import static com.zoddl.utils.AppConstant.LOCAL_BROADCAST_FOR_ALERT;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertFragment extends BaseFragment {

    private FragmentAlertBinding alertBinding;
    private Context context;
    private NotificationTagAdapter mAdapter;
    private List<Message> mList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alertBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert, container, false);

        getDBNotificationData();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter(LOCAL_BROADCAST_FOR_ALERT));

        return alertBinding.getRoot();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(_MESSAGE);
            if (message.equalsIgnoreCase(_SUCCESS)) {
                if (mList.size()!=0){
                    mList.clear();
                }
                getDBNotificationData();
            }
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void getDBNotificationData() {
        MyRoomDatabase db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();
        mList = db.getMessageDao().getAll();
        mAdapter = new NotificationTagAdapter(context,mList);
        alertBinding.notificationRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        alertBinding.notificationRv.setAdapter(mAdapter);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public String getTagName() {
        return AlertFragment.class.getSimpleName();
    }
}
