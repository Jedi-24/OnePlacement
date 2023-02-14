package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.databinding.FragmentRolesBinding;

public class RolesFragment extends Fragment {
    FragmentRolesBinding mBinding;
    public RolesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRolesBinding.inflate(inflater, container, false);

//        Set<RoleDto> roles = UserInstance.getRoles();
//        String role = "";
//        for (RoleDto roleDto : roles) {
//            role = roleDto.getRole_name();
//        }

//        mBinding.bottomNav.getMenu().getItem(2).setTitle(role + "s");;
//        if (UserInstance.getRoles().size() > 1) {
//            mBinding.bottomNav.getMenu().getItem(3).setVisible(true);
//        }
//
//        mBinding.bottomNav.getMenu().findItem(R.id.set_role).setChecked(true);
//
//        mBinding.bottomNav.setOnItemSelectedListener(item1 -> {
//            if (item1.getItemId() == R.id.home) {
//                NavOptions.Builder navBuilder = new NavOptions.Builder();
//                NavOptions navOptions = navBuilder.setPopUpTo(R.id.rolesFragment, true).build();
//                NavHostFragment.findNavController(RolesFragment.this).navigate(R.id.homeFragment, null, navOptions);
//                return true;
//            }
//            if (item1.getItemId() == R.id.companies) {
//                NavOptions.Builder navBuilder = new NavOptions.Builder();
//                NavOptions navOptions = navBuilder.setPopUpTo(R.id.rolesFragment, true).build();
//                NavHostFragment.findNavController(RolesFragment.this).navigate(R.id.companyFragment, null, navOptions);
//                return true;
//            }
//
//            if (item1.getItemId() == R.id.set_role) {
//                NavOptions.Builder navBuilder = new NavOptions.Builder();
//                NavOptions navOptions = navBuilder.setPopUpTo(R.id.rolesFragment, true).build();
//                NavHostFragment.findNavController(RolesFragment.this).navigate(R.id.rolesFragment, null, navOptions);
//                return true;
//            }
//
//            if (item1.getItemId() == R.id.admin) {
//                NavOptions.Builder navBuilder = new NavOptions.Builder();
//                NavOptions navOptions = navBuilder.setPopUpTo(R.id.rolesFragment, true).build();
//                NavHostFragment.findNavController(RolesFragment.this).navigate(R.id.adminFragment, null, navOptions);
//                return true;
//            }
//            return false;
//        });

        return mBinding.getRoot();
    }
}