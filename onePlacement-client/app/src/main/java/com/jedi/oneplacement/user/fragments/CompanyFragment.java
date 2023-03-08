package com.jedi.oneplacement.user.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.activities.MainActivity;
import com.jedi.oneplacement.databinding.FragmentCompanyBinding;
import com.jedi.oneplacement.databinding.LockedLayoutBinding;
import com.jedi.oneplacement.user.utils.VPadapter;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

public class CompanyFragment extends Fragment {
    FragmentCompanyBinding mBinding;
    LockedLayoutBinding lockedBinding;

    VPadapter vPadapter;

    public CompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCompanyBinding.inflate(inflater, container, false);

        if(Integer.parseInt(UserInstance.getCreditPts()) <= 4 || UserInstance.getRoleStatus().matches("OFFERED")){
            lockedBinding = LockedLayoutBinding.inflate(inflater, container, false);
            if(Integer.parseInt(UserInstance.getCreditPts()) <= 4)
                lockedBinding.msg.setText("NOT ENOUGH CREDIT POINTS !");
            else {
                lockedBinding.profileLocked.setImageResource(R.drawable.checked);
                lockedBinding.msg.setText("ALREADY PLACED !");
            }
            return lockedBinding.getRoot();
        }

        Bitmap b = Cache.readImgFromCache(requireContext(), 0);
        if (b != null) mBinding.layout.userPhoto.setImageBitmap(b);

        mBinding.layout.userPhoto.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.userProfileFragment);
        });
        MainActivity.setCheck();

        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("OPENINGS"));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("REGISTERED"));

        vPadapter = new VPadapter(getChildFragmentManager(), getLifecycle());
        mBinding.viewPager.setAdapter(vPadapter);

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