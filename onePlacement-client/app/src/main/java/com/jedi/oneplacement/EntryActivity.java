package com.jedi.oneplacement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.jedi.oneplacement.fragments.HomeFragment;
import com.jedi.oneplacement.fragments.LoginFragment;
import com.jedi.oneplacement.fragments.RegisterFragment;

import com.jedi.oneplacement.retrofit.AuthApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

import java.util.HashMap;
import java.util.Map;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        checkUserSession();
    }

    private void checkUserSession() {
        // retrieve token from shared preferences:
        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);

        UserInstance.updateJwtToken(token, new UserInstance.FetchListener() {
            @Override
            public void onFetch() {
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                NavOptions navOptions = navBuilder.setPopUpTo(R.id.loginFragment, true).build();
                NavController navController = Navigation.findNavController(EntryActivity.this,R.id.fragmentContainerView);
                navController.navigate(R.id.homeFragment,null,navOptions);
            }

            @Override
            public void onError(int code) {
                // in any case, BAD REQUEST | UNSUCCESSFUL REQUEST :
//                loadLoginFragment();
            }
        });
    }
}