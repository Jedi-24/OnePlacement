package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentHomeBinding;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;

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

        Cache.getImage(requireContext(),mBinding.userProfileImg);

        if(UserInstance.getBranch()!=null)
            mBinding.userBranch.setText(UserInstance.getBranch());
        mBinding.role.setText(UserInstance.getRole().iterator().next().getRole_name());

        mBinding.toolBar.userPhoto.setOnClickListener(v->{
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.userProfileFragment);
        });

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