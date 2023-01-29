package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.jedi.oneplacement.EntryActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.utils.AppConstants;

public class HomeFragment extends Fragment {
    TextView mHome;
    MaterialButton mLogout;

    EntryActivity mEntryActivity;

    public HomeFragment(EntryActivity entryActivity) {
        // Required empty public constructor
        this.mEntryActivity = entryActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        retrieveDataFromPreferences();

        mLogout.setOnClickListener(v->{
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(AppConstants.JWT, "Jedi_24").apply();
            mEntryActivity.loadLoginFragment();
        });

        return view;
    }

    private void retrieveDataFromPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String fromJson = sharedPreferences.getString(AppConstants.JSON_DATA, null);
        User userdata = gson.fromJson(fromJson, User.class);
        mHome.setText("Hello, " + userdata.getName());
    }

    private void initViews(View view) {
        mHome = view.findViewById(R.id.home_txt);
        mLogout = view.findViewById(R.id.log_out_btn);
    }

}