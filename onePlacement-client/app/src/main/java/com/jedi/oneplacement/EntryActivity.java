package com.jedi.oneplacement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.jedi.oneplacement.fragments.HomeFragment;
import com.jedi.oneplacement.fragments.LoginFragment;
import com.jedi.oneplacement.fragments.RegisterFragment;

import com.jedi.oneplacement.retrofit.AuthApiImpl;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.HashMap;
import java.util.Map;

public class EntryActivity extends AppCompatActivity {
    private static final String TAG = "EntryActivity";
    private Map<String, Object> mUserdata = new HashMap<>();

    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        loginFragment = new LoginFragment(this);
        registerFragment = new RegisterFragment(this);
        homeFragment = new HomeFragment(this);

        checkUserSession();
    }

    public void loadRegFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment, registerFragment).commit();
    }

    public void loadLoginFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment, loginFragment).commit();
    }

    public void loadHomeFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment, homeFragment).commit();
    }

    private void checkUserSession() {
        // retrieve token from shared preferences:
        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);

        AuthApiImpl.checkUser("Bearer " + token, new AuthApiImpl.ApiCallListener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                mUserdata = response;
                String toJson = new Gson().toJson(mUserdata.get("Authenticated: "));
                Log.d(TAG, "onResponse: " + toJson);
                sharedPreferences.edit().putString(AppConstants.JSON_DATA, toJson).apply();
                loadHomeFragment();
            }

            @Override
            public void onFailure(int code) {
                // in any case, BAD REQUEST | UNSUCCESSFUL REQUEST :
                loadLoginFragment();
            }
        });
    }
}