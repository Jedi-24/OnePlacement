package com.jedi.oneplacement.admin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jedi.oneplacement.databinding.FragmentUserBinding;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.User;
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
        requireActivity().getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            // We use a String here, but any type that can be put in a Bundle is supported
            String result = bundle.getString("bundleKey");
            // Do something with the result
            mUser = new Gson().fromJson(result, User.class);
            Log.d(TAG, "onCreateView: " + mUser.toString());
//            mBinding.test.setText(mUser.toString());
        });

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);
        if (mUser != null) {
            Log.d(TAG, "onCreateView: okokok");
            ApiImpl.getImage(mUser.getId(), token, new ApiImpl.ApiCallListener<FileResponse>() {
                @Override
                public void onResponse(FileResponse response) {
                    byte[] byteData = Base64.decode(response.getFileName(), 0);
                    Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
//                .setImageBitmap(bm);
//                tbImg.setImageBitmap(bm);

                }

                @Override
                public void onFailure(int code) {
                }
            });
            ApiImpl.getResume(mUser.getId(), token, new ApiImpl.ApiCallListener<FileResponse>() {
                @Override
                public void onResponse(FileResponse response) {
                }

                @Override
                public void onFailure(int code) {
                }
            });
        }
    }
}