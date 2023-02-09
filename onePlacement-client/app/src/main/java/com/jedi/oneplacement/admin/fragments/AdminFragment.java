package com.jedi.oneplacement.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentAdminBinding;
import com.jedi.oneplacement.utils.UserInstance;

public class AdminFragment extends Fragment {
    FragmentAdminBinding mBinding;
    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAdminBinding.inflate(inflater, container, false);

        if (UserInstance.getRoles().size() > 1) {
            mBinding.bottomNav.getMenu().getItem(3).setVisible(true);
        }
        mBinding.bottomNav.getMenu().findItem(R.id.admin).setChecked(true);

        mBinding.bottomNav.setOnItemSelectedListener(item1 -> {
            if (item1.getItemId() == R.id.home) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.adminFragment, true).build();
                NavHostFragment.findNavController(AdminFragment.this).navigate(R.id.homeFragment, null, navOptions);
                return true;
            }
            if (item1.getItemId() == R.id.companies) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.adminFragment, true).build();
                NavHostFragment.findNavController(AdminFragment.this).navigate(R.id.companyFragment, null, navOptions);
                return true;
            }

            if (item1.getItemId() == R.id.set_role) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.adminFragment, true).build();
                NavHostFragment.findNavController(AdminFragment.this).navigate(R.id.rolesFragment, null, navOptions);
                return true;
            }

            if (item1.getItemId() == R.id.admin) {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.adminFragment, true).build();
                NavHostFragment.findNavController(AdminFragment.this).navigate(R.id.adminFragment, null, navOptions);
                return true;
            }

            return false;
        });

        return mBinding.getRoot();
    }
}