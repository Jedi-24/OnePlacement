package com.jedi.oneplacement.admin.fragments;

import android.app.AlertDialog;
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
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentUserBinding;
import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

import java.util.Set;

public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";
    FragmentUserBinding mBinding;
    User mUser;
    boolean isAllFabVisible;

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
            setUserData();
        });
        mBinding.layout.userPhoto.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);

        isAllFabVisible = false;
        mBinding.editCredFab.setVisibility(View.GONE);
        mBinding.verifyFab.setVisibility(View.GONE);
        mBinding.editCredText.setVisibility(View.GONE);
        mBinding.verifyText.setVisibility(View.GONE);

        mBinding.editFab.shrink();
        mBinding.editFab.setOnClickListener(v -> {
            if (!isAllFabVisible) {
                mBinding.editCredFab.show();
                mBinding.verifyFab.show();
                mBinding.editCredText.setVisibility(View.VISIBLE);
                mBinding.verifyText.setVisibility(View.VISIBLE);

                mBinding.editFab.extend();
                isAllFabVisible = true;
            } else {
                mBinding.editCredFab.hide();
                mBinding.verifyFab.hide();
                mBinding.editCredText.setVisibility(View.GONE);
                mBinding.verifyText.setVisibility(View.GONE);

                mBinding.editFab.shrink();
                isAllFabVisible = false;
            }
        });

        mBinding.verifyFab.setOnClickListener(v -> {
            ApiImpl.verifyProfile(mUser.getId(), jwt, new ApiImpl.ApiCallListener<ApiResponse>() {
                @Override
                public void onResponse(ApiResponse response) {
                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    // to update user verified status:
                    mBinding.profileStatusIcon.setImageResource(R.drawable.verified_svgrepo_com);
                    mBinding.profileStatusTxt.setText(AppConstants.VERIFIED);
                }

                @Override
                public void onFailure(int code) {
                    Toast.makeText(requireContext(), "Could not Verify Profile !", Toast.LENGTH_SHORT).show();
                }
            });
        });

        mBinding.editCredFab.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setTitle("CREDITS :");
            ViewGroup viewGroup = getView().findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.num_picker, viewGroup, false);

            NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(10);

            MaterialButton mBtn = dialogView.findViewById(R.id.set_credits);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            mBtn.setOnClickListener(v1 -> {
                ApiImpl.setCredits(numberPicker.getValue(), mUser.getId(), jwt, new ApiImpl.ApiCallListener<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        // changed successfully:
                        Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        // to update the UI:

                        mBinding.creditPts.setText(Integer.toString(numberPicker.getValue()));
                    }

                    @Override
                    public void onFailure(int code) {
                        Toast.makeText(requireContext(), "Failed! " + code, Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.dismiss();
            });
            alertDialog.show();
        });
        return mBinding.getRoot();
    }

    private void setUserData() {
        mBinding.userName.setText(mUser.getName());
        mBinding.userRegNo.setText(mUser.getRegNo());
        mBinding.creditPts.setText(mUser.getTpoCredits());
        mBinding.userBranch.setText(mUser.getBranch());
        mBinding.userEmail.setText(mUser.getEmail());
        String profileStatus = mUser.getProfileStatus();
        mBinding.profileStatusTxt.setText(profileStatus);

        if (profileStatus.matches(AppConstants.VERIFIED))
            mBinding.profileStatusIcon.setImageResource(R.drawable.verified_svgrepo_com);
        mBinding.roleTxt.setText(mUser.getRoleStatus());

        Set<RoleDto> roles = mUser.getRoles();
        String role = "";
        for (RoleDto roleDto : roles) {
            role = roleDto.getRole_name();
        }

        if(role.length() > 5)
            mBinding.role.setText(role.substring(5));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUser != null) {

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
            String jwt = sharedPreferences.getString(AppConstants.JWT, null);
            ApiImpl.getImage(mUser.getId(), jwt, new ApiImpl.ApiCallListener<FileResponse>() {
                @Override
                public void onResponse(FileResponse response) {
                    Log.d(TAG, "onResponse: " + response.getFileName());
                    byte[] byteData = Base64.decode(response.getFileName(), 0);
                    Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
                    mBinding.userProfileImg.setImageBitmap(bm);
                }

                @Override
                public void onFailure(int code) {
                }
            });
//            ApiImpl.getResume(mUser.getId(), jwt, new ApiImpl.ApiCallListener<FileResponse>() {
//                @Override
//                public void onResponse(FileResponse response) {
//                }
//
//                @Override
//                public void onFailure(int code) {
//                }
//            });
        }
    }
}