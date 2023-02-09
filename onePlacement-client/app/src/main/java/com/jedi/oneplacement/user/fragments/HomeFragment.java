package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.jedi.oneplacement.EntryActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.fragments.AdminFragment;
import com.jedi.oneplacement.databinding.FragmentHomeBinding;
import com.jedi.oneplacement.user.payloads.RoleDto;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

import java.util.Iterator;
import java.util.Set;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    FragmentHomeBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        mBinding.userName.setText(UserInstance.getName());
        mBinding.userRegNo.setText(UserInstance.getRegNo());
        mBinding.creditPts.setText(UserInstance.getCreditPts());
        mBinding.profileStatusTxt.setText(UserInstance.getProfileStatus());
        mBinding.roleTxt.setText(UserInstance.getRoleStatus());

//        String role = UserInstance.getRole().getRole_name();
        Set<RoleDto> roles = UserInstance.getRoles();
        String role = "";
        for (RoleDto roleDto : roles) {
            role = roleDto.getRole_name();
        }

        mBinding.bottomNav.getMenu().getItem(2).setTitle(role + "s");
        if (UserInstance.getRoles().size() > 1) {
            mBinding.bottomNav.getMenu().getItem(3).setVisible(true);
        }

        mBinding.bottomNav.setOnItemSelectedListener(item1 -> {
            Log.d(TAG, "onNavigationItemSelected: " + item1.getItemId());
            if (item1.getItemId() == R.id.home) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.homeFragment, null, navOptions);
                return true;
            }
            if (item1.getItemId() == R.id.companies) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.companyFragment, null, navOptions);
                return true;
            }

            if (item1.getItemId() == R.id.set_role) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.rolesFragment, null, navOptions);
                return true;
            }

            if (item1.getItemId() == R.id.admin) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.adminFragment, null, navOptions);
                return true;
            }
            return false;
        });

        Cache.getImage(requireContext(), mBinding.userProfileImg, mBinding.layout.userPhoto);

        if (UserInstance.getBranch() != null)
            mBinding.userBranch.setText(UserInstance.getBranch());

        mBinding.role.setText(role.substring(5));
        mBinding.layout.userPhoto.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.userProfileFragment);
        });

        return mBinding.getRoot();
    }
}