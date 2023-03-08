package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jedi.oneplacement.data.Repository;
import com.jedi.oneplacement.databinding.FragmentPdfBinding;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

public class PdfFragment extends Fragment {
    FragmentPdfBinding mBinding;
    Integer userId = 0;

    public PdfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPdfBinding.inflate(inflater, container, false);
        requireActivity().getSupportFragmentManager().setFragmentResultListener("userIdKey", this, (requestKey, bundle) -> {
            // We use a String here, but any type that can be put in a Bundle is supported
            int result = bundle.getInt("userId");
            // Do something with the result:
            userId = result;
            boolean resume = Repository.getResume(requireContext(), mBinding.pdfView, userId == 0 ? UserInstance.getId() : userId);
            if (!resume) {
                mBinding.pdfView.setVisibility(View.GONE);
                mBinding.noResume.setVisibility(View.VISIBLE);
            }
        });

        return mBinding.getRoot();
    }
}