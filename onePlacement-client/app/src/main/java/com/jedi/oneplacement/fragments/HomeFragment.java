package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.jedi.oneplacement.EntryActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentHomeBinding;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mBinding.homeTxt.setText("Hello, " + UserInstance.getName());

        mBinding.logOutBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(AppConstants.JWT, "Jedi_24").apply();

            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.loginFragment, null, navOptions);
        });
        return mBinding.getRoot();
    }
}