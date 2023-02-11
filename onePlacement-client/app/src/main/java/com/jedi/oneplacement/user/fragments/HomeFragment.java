package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentHomeBinding;
import com.jedi.oneplacement.user.payloads.RoleDto;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

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

        Set<RoleDto> roles = UserInstance.getRoles();
        String role = "";
        for (RoleDto roleDto : roles) {
            role = roleDto.getRole_name();
        }

        Cache.getImage(requireContext(), mBinding.userProfileImg, mBinding.layout.userPhoto);

        if (UserInstance.getBranch() != null)
            mBinding.userBranch.setText(UserInstance.getBranch());
        if(role.length() > 5)
            mBinding.role.setText(role.substring(5));

        mBinding.layout.userPhoto.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(HomeFragment.this);
            navController.navigate(R.id.userProfileFragment);
        });

        return mBinding.getRoot();
    }
}