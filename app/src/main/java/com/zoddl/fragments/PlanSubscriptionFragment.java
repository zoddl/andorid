package com.zoddl.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zoddl.R;
import com.zoddl.adapters.CurrentPlanAdapter;
import com.zoddl.adapters.PaymentHistroyAdapter;
import com.zoddl.databinding.FragmentPlanSubscriptionBinding;


public class PlanSubscriptionFragment extends BaseFragment {

    private FragmentPlanSubscriptionBinding binding;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_plan_subscription, container, false);

        setRecyclerView();
        setListeners();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        CurrentPlanAdapter currentPlanAdapter = new CurrentPlanAdapter(context);
        binding.rvPlanDetail.setLayoutManager(new LinearLayoutManager(context));
        binding.rvPlanDetail.setAdapter(currentPlanAdapter);
        binding.rvPlanDetail.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        binding.rvPlanDetail.setNestedScrollingEnabled(false);

        PaymentHistroyAdapter paymentHistroyAdapter = new PaymentHistroyAdapter(context);
        binding.rvPaymentHistory.setLayoutManager(new LinearLayoutManager(context));
        binding.rvPaymentHistory.setAdapter(paymentHistroyAdapter);
        binding.rvPaymentHistory.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        binding.rvPaymentHistory.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_now:
               if (checkValidation())
               {
                   Toast.makeText(getContext(), "Plan success.", Toast.LENGTH_SHORT).show();
               }
                break;
        }
    }

    @Override
    public void setListeners() {
        binding.btnBuyNow.setOnClickListener(this);
    }

    private boolean checkValidation() {
        if (!binding.cbAgreeTermsConditions.isChecked()) {
            binding.cbAgreeTermsConditions.requestFocus();
            binding.cbAgreeTermsConditionsError.setText("*Please agree with terms and conditions.");
            binding.cbAgreeTermsConditionsError.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.cbAgreeTermsConditionsError.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    public String getTagName() {
        return PlanSubscriptionFragment.class.getSimpleName();
    }
}
