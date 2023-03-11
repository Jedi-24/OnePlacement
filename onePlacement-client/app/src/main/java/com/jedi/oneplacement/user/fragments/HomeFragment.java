package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.data.Repository;
import com.jedi.oneplacement.databinding.FragmentHomeBinding;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.data.UserInstance;

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
        String profileStatus = UserInstance.getProfileStatus();
        mBinding.profileStatusTxt.setText(profileStatus);

        if(profileStatus.matches(AppConstants.VERIFIED))
            mBinding.profileStatusIcon.setImageResource(R.drawable.verified_svgrepo_com);
        mBinding.roleTxt.setText(UserInstance.getRoleStatus());
        Set<RoleDto> roles = UserInstance.getRoles();
        String role = "";
        for (RoleDto roleDto : roles) {
            role = roleDto.getRoleName();
        }

        Repository.getImage(requireContext(),0, mBinding.userProfileImg, mBinding.layout.userPhoto);

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