package com.jedi.oneplacement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jedi.oneplacement.fragments.UserProfileFragment;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

public class EntryActivity extends AppCompatActivity {
    private static final String TAG = "EntryActivity";
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
                Cache.removeImgFromCache(EntryActivity.this);
                Cache.removeResumeFromCache(EntryActivity.this);

                Toast.makeText(EntryActivity.this, "maa chudaoo", Toast.LENGTH_SHORT).show();
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