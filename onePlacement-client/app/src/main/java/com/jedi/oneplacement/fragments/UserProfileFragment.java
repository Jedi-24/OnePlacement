package com.jedi.oneplacement.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentHomeBinding;
import com.jedi.oneplacement.databinding.FragmentUserProfileBinding;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    FragmentUserProfileBinding mBinding;
    Boolean editable;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
//        mBinding.profilePhoto.setImageBitmap(Cache.getImageCache(UserInstance.getId()));
        Bitmap b = Cache.readFromCache(getContext());
        if(b!=null){
            mBinding.profilePhoto.setImageBitmap(b);
            Toast.makeText(requireContext(), "Image is loaded from Internal Cache!", Toast.LENGTH_SHORT).show();
        }

        toggleEditables(false);

        mBinding.profilePhoto.setOnClickListener(v->{
            // open a bottom view to select image from gallery:

        });

        mBinding.editView.setOnClickListener(v->{
            Log.d(TAG, "onCreateView: hehehehe");
            if(!editable){
                mBinding.editView.setText("Save");
                toggleEditables(true);
            }
            else{
                mBinding.editView.setText("Edit");
                //todo: // send request to server:
                toggleEditables(false);
            }
        });

        return mBinding.getRoot();
    }

    private void toggleEditables(Boolean b){
        mBinding.profilePhoto.setClickable(b);
        mBinding.userBranch.setEnabled(b);
        mBinding.email.setEnabled(b);
        mBinding.mNumber.setEnabled(b);
        editable = b;
    }
}