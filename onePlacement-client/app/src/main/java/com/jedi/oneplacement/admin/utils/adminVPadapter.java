package com.jedi.oneplacement.admin.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jedi.oneplacement.admin.fragments.AddCompanyFragment;
import com.jedi.oneplacement.admin.fragments.UserListFragment;

public class adminVPadapter extends FragmentStateAdapter {
    public adminVPadapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1) return new AddCompanyFragment();
        return new UserListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}