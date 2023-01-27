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
import com.jedi.oneplacement.LoginActivity;
import com.jedi.oneplacement.MainActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.UserLoginInfo;
import com.jedi.oneplacement.retrofit.AuthApi;
import com.jedi.oneplacement.retrofit.RetrofitInitializer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    
    TextInputEditText mUsername,mPassword;
    MaterialButton mBtn;
    TextView mReg;
    LoginActivity mLoginActivity;

    private String mUser,password;

    public LoginFragment(LoginActivity loginActivity) {
        this.mLoginActivity = loginActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);

        mBtn.setOnClickListener(v->{
            mUser = mUsername.getText().toString();
            password = mPassword.getText().toString();

            mUsername.setText("");
            mPassword.setText("");
            callLoginApi(mUser,password);
        });

        mReg.setOnClickListener(v -> mLoginActivity.loadRegFragment());

        return view;
    }

    private void callLoginApi(String mUser, String password) {
        RetrofitInitializer retrofitInitializer = new RetrofitInitializer();
        AuthApi auth = retrofitInitializer.getRetrofit().create(AuthApi.class);

        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUsername(mUser);
        userLoginInfo.setPassword(password);

        auth.loginUser(userLoginInfo)
                .enqueue(new Callback<JwtAuthResponse>() {
                    @Override
                    public void onResponse(Call<JwtAuthResponse> call, Response<JwtAuthResponse> response) {
                        if(response.body()!=null){
                            String jwtToken = response.body().getToken();
                            if(!(jwtToken.isEmpty())){
                                // store token in shared preferences:
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ONE_PLACEMENT", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("JWT", jwtToken).apply();

                                startActivity(new Intent(requireContext(), MainActivity.class));
                            }
                        }
                        else{
                            Toast.makeText(mLoginActivity, "Invalid Credentials, try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JwtAuthResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
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