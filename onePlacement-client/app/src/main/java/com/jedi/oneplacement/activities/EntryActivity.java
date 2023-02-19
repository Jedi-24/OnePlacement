package com.jedi.oneplacement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

public class EntryActivity extends AppCompatActivity {
    private static final String TAG = "EntryActivity";
    BottomNavigationView bottomNavigationView;
    String fg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        fg = getIntent().getStringExtra(AppConstants.APP);
        checkUserSession(fg);
    }

    private void checkUserSession(String fg) {
        // retrieve token from shared preferences:
        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);
        Log.d(TAG, "checkUserSession: " + token);
        UserInstance.updateJwtToken(token, new UserInstance.FetchListener() {
            @Override
            public void onFetch() {
                Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                intent.putExtra("key", fg);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int code) {
                Cache.removeImgFromCache(EntryActivity.this);
                Cache.removeResumeFromCache(EntryActivity.this);
                // in any case, BAD REQUEST | UNSUCCESSFUL REQUEST :
//                loadLoginFragment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back presseed");
        super.onBackPressed();
    }
}