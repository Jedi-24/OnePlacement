package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.databinding.FragmentPdfBinding;
import com.jedi.oneplacement.user.utils.Cache;

public class PdfFragment extends Fragment {
    private static final String TAG = "PdfFragment";
    FragmentPdfBinding mBinding;

    public PdfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPdfBinding.inflate(inflater, container, false);

        boolean resume = Cache.getResume(requireContext(),mBinding.pdfView);

        if(!resume) {
            mBinding.pdfView.setVisibility(View.GONE);
            mBinding.noResume.setVisibility(View.VISIBLE);
        }
        return mBinding.getRoot();
    }
}