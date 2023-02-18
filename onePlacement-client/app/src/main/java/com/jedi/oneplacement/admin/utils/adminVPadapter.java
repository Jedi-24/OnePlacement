package com.jedi.oneplacement.admin.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jedi.oneplacement.admin.fragments.AddCompanyFragment;
import com.jedi.oneplacement.admin.fragments.UserListFragment;
import com.jedi.oneplacement.payloads.User;

public class adminVPadapter extends FragmentStateAdapter {

    private UserListFragment userListFragment;

    public adminVPadapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        userListFragment = new UserListFragment();
        if(position == 1) return new AddCompanyFragment();
        return userListFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public UserListFragment getUserFragment() {
        return userListFragment;
    }
}