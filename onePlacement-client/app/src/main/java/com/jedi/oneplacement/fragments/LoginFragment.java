package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jedi.oneplacement.EntryActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.retrofit.AuthApiImpl;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    TextInputEditText mUsername, mPassword;
    MaterialButton mBtn;
    TextView mReg;
    EntryActivity mEntryActivity;

    private String mUser, password;
    private Map<String, Object> mUserdata = new HashMap<>();

    public LoginFragment(EntryActivity entryActivity) {
        this.mEntryActivity = entryActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);

        mBtn.setOnClickListener(v -> {
            mUser = mUsername.getText().toString();
            password = mPassword.getText().toString();

            mUsername.setText("");
            mPassword.setText("");
            callLoginApi(mUser, password);
        });

        mReg.setOnClickListener(v -> mEntryActivity.loadRegFragment());

        return view;
    }

    private void callLoginApi(String mUser, String password) {
        AuthApiImpl.loginUser(mUser, password, new AuthApiImpl.ApiCallListener<JwtAuthResponse>() {
            @Override
            public void onResponse(JwtAuthResponse response) {
                String token = response.getToken();
                Log.d(TAG, "onResponse: " + token);
                if (!(token.isEmpty())) {
                    // store token in shared preferences:
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(AppConstants.JWT, token).apply();
                    redirectToHome(token);
                }
            }

            @Override
            public void onFailure(int code) {
                if (code == -1) {
                    // no internet
                    Toast.makeText(requireContext(), code + ": Error could not login, check your Internet.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), code + ": Bad Request.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectToHome(String token) {
        // retrieve token from shared preferences:
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);

        AuthApiImpl.checkUser("Bearer " + token, new AuthApiImpl.ApiCallListener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                mUserdata = response;
                String toJson = new Gson().toJson(mUserdata.get("Authenticated: "));
                Log.d(TAG, "onResponse: " + toJson);
                sharedPreferences.edit().putString(AppConstants.JSON_DATA, toJson).apply();
                mEntryActivity.loadHomeFragment();
            }

            @Override
            public void onFailure(int code) {
                // in any case, BAD REQUEST | UNSUCCESSFUL REQUEST :
                mEntryActivity.loadLoginFragment();
            }
        });
    }



    private void initViews(View view) {
        mUsername = view.findViewById(R.id.user_name);
        mPassword = view.findViewById(R.id.password);
        mBtn = view.findViewById(R.id.login_btn);
        mReg = view.findViewById(R.id.reg_txt);
    }
}