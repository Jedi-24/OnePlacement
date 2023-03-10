package com.jedi.oneplacement.user.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jedi.oneplacement.user.fragments.OpeningsFragment;
import com.jedi.oneplacement.user.fragments.RegisteredCompaniesFragment;

public class VPadapter extends FragmentStateAdapter {
    public VPadapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1) return new RegisteredCompaniesFragment();
        return new OpeningsFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}