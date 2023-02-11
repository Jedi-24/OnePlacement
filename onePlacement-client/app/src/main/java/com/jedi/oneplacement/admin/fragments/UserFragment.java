package com.jedi.oneplacement.admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentUserBinding;
import com.jedi.oneplacement.user.payloads.User;
import com.jedi.oneplacement.utils.AppConstants;

public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";
    FragmentUserBinding mBinding;
    User mUser;
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserBinding.inflate(inflater, container, false);

        requireActivity().getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.d(TAG, "onFragmentResult: hereeeeeeeeee");
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
                mUser = new Gson().fromJson(result, User.class);
                mBinding.test.setText(mUser.toString());
            }
        });
        return mBinding.getRoot();
    }
}