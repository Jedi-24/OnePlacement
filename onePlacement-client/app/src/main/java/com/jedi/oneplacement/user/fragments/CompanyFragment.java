package com.jedi.oneplacement.user.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.fragments.AdminFragment;
import com.jedi.oneplacement.databinding.FragmentCompanyBinding;
import com.jedi.oneplacement.user.payloads.RoleDto;
import com.jedi.oneplacement.user.utils.VPadapter;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

import java.util.Iterator;
import java.util.Set;

public class CompanyFragment extends Fragment {
    FragmentCompanyBinding mBinding;
    VPadapter vPadapter;

    public CompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCompanyBinding.inflate(inflater, container, false);

        Bitmap b = Cache.readFromCache(requireContext());
        if (b != null) mBinding.layout.userPhoto.setImageBitmap(b);

        mBinding.layout.userPhoto.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.userProfileFragment);
        });

//        String role = UserInstance.getRole().getRole_name();
        Set<RoleDto> roles = UserInstance.getRoles();
        String role = "";
        for (RoleDto roleDto : roles) {
            role = roleDto.getRole_name();
        }

        if (UserInstance.getRoles().size() > 1) {
            mBinding.bottomNav.getMenu().getItem(3).setVisible(true);
        }

        mBinding.bottomNav.getMenu().getItem(2).setTitle(role + "s");
        ;
        mBinding.bottomNav.getMenu().findItem(R.id.companies).setChecked(true);

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

        mBinding.bottomNav.setOnItemSelectedListener(item1 -> {
            if (item1.getItemId() == R.id.home) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.companyFragment, true).build();
                NavHostFragment.findNavController(CompanyFragment.this).navigate(R.id.homeFragment, null, navOptions);
                return true;
            }
            if (item1.getItemId() == R.id.companies) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.companyFragment, true).build();
                NavHostFragment.findNavController(CompanyFragment.this).navigate(R.id.companyFragment, null, navOptions);
                return true;
            }

            if (item1.getItemId() == R.id.set_role) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.companyFragment, true).build();
                NavHostFragment.findNavController(CompanyFragment.this).navigate(R.id.rolesFragment, null, navOptions);
                return true;
            }

            if (item1.getItemId() == R.id.admin) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.companyFragment, true).build();
                NavHostFragment.findNavController(CompanyFragment.this).navigate(R.id.adminFragment, null, navOptions);
                return true;
            }
            return false;
        });

        return mBinding.getRoot();
    }
}