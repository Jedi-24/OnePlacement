package com.jedi.oneplacement.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.utils.adminVPadapter;
import com.jedi.oneplacement.databinding.FragmentAdminBinding;
import com.jedi.oneplacement.user.utils.VPadapter;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

public class AdminFragment extends Fragment {
    FragmentAdminBinding mBinding;
    adminVPadapter adminVPadapter;
    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAdminBinding.inflate(inflater, container, false);
        mBinding.layout.userPhoto.setVisibility(View.GONE);
        mBinding.layout.title.setVisibility(View.VISIBLE);
        mBinding.layout.title.setText(AppConstants.ADMIN);

        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("ALL USERS"));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("ADD COMPANY"));

        adminVPadapter = new adminVPadapter(getChildFragmentManager(), getLifecycle());
        mBinding.viewPager.setAdapter(adminVPadapter);

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mBinding.tabLayout.selectTab(mBinding.tabLayout.getTabAt(position));
            }
        });

        return mBinding.getRoot();
    }
}