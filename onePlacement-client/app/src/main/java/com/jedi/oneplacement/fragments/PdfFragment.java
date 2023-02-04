package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.listener.OnLongPressListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jedi.oneplacement.databinding.FragmentPdfBinding;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

import java.io.File;

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