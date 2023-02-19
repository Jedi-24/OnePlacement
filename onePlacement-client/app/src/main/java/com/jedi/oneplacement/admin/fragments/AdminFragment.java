package com.jedi.oneplacement.admin.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.utils.adminVPadapter;
import com.jedi.oneplacement.databinding.FragmentAdminBinding;
import com.jedi.oneplacement.utils.AppConstants;

public class AdminFragment extends Fragment {
    private static final String TAG = "AdminFragment";
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

        mBinding.layout.toolBar.inflateMenu(R.menu.search_menu);

        mBinding.layout.toolBar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.search){
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint("Type here to Search Users...");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        UserListFragment.searcher(adminVPadapter.getUserFragment(), query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        UserListFragment.reset(adminVPadapter.getUserFragment());
                        return true;
                    }
                });


            }
            return false;
        });

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPager.setCurrentItem(tab.getPosition(), true);
                if(tab.getPosition() == 1){
                    mBinding.layout.toolBar.getMenu().clear();
                }
                else
                    mBinding.layout.toolBar.inflateMenu(R.menu.search_menu);
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

//        UserListFragment.reset(adminVPadapter.getUserFragment());

        return mBinding.getRoot();
    }



}