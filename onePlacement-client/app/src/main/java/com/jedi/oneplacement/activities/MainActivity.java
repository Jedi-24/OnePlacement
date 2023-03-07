package com.jedi.oneplacement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.fragments.AdminFragment;
import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.data.Repository;
import com.jedi.oneplacement.databinding.ActivityMainBinding;
import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.user.fragments.CompanyFragment;
import com.jedi.oneplacement.user.fragments.HomeFragment;
import com.jedi.oneplacement.user.fragments.RolesFragment;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static ActivityMainBinding mBinding;
    HomeFragment homeFragment;
    CompanyFragment companyFragment;
    RolesFragment rolesFragment;
    AdminFragment adminFragment;
    int popUpto = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        homeFragment = new HomeFragment();
        companyFragment = new CompanyFragment();
        rolesFragment = new RolesFragment();
        adminFragment = new AdminFragment();

        Set<RoleDto> roles = UserInstance.getRoles();
        String role = "";
        for (RoleDto roleDto : roles) {
            role = roleDto.getRole_name();
        }

        if(getIntent().getStringExtra("key")!=null)
            redirectToOpeningsFragment();

        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String devT = sharedPreferences.getString(AppConstants.DEV_TOKEN, null);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    String jwt = sharedPreferences.getString(AppConstants.JWT, null);

                    if(devT!=null && devT.matches(token))
                        return;
                    ApiImpl.setDeviceToken(token, jwt, new ApiImpl.ApiCallListener<ApiResponse>() {
                        @Override
                        public void onResponse(ApiResponse response) {
                            sharedPreferences.edit().putString(AppConstants.DEV_TOKEN, token).apply();
                        }

                        @Override
                        public void onFailure(int code) {
                        }
                    });
                });

        if (UserInstance.getRoles().size() > 1)
            mBinding.bottomNav.getMenu().getItem(3).setVisible(true);

        // todo: fetching companies/users here was: irrelevant.

        mBinding.bottomNav.getMenu().getItem(2).setTitle(role + "s");
        mBinding.bottomNav.setOnItemSelectedListener(item1 -> {
            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setPopUpTo(popUpto, true).build();

            NavController navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainer);
            if (item1.getItemId() == R.id.home) {
                navController.navigate(R.id.homeFragment,null,navOptions);
                popUpto = R.id.homeFragment;
                return true;
            }
            if (item1.getItemId() == R.id.companies) {
                navController.navigate(R.id.companyFragment,null,navOptions);
                popUpto = R.id.companyFragment;
                return true;
            }

            if (item1.getItemId() == R.id.set_role) {
                navController.navigate(R.id.rolesFragment,null,navOptions);
                popUpto = R.id.rolesFragment;
                return true;
            }

            if (item1.getItemId() == R.id.admin) {
                navController.navigate(R.id.adminFragment,null,navOptions);
                popUpto = R.id.adminFragment;
                return true;
            }
            return false;
        });
    }

    private void redirectToOpeningsFragment() {
        Bundle result = new Bundle();
        result.putString("bundleKey", AppConstants.PING_SERVER);
        getSupportFragmentManager().setFragmentResult("requestKey", result);
        // to open company openings fragment: -->
        NavOptions.Builder navBuilder = new NavOptions.Builder();
        NavOptions navOptions = navBuilder.setPopUpTo(popUpto, true).build();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainer);
        navController.navigate(R.id.companyFragment,null,navOptions);
        popUpto = R.id.companyFragment;
    }

    public static void setCheck(){
        mBinding.bottomNav.getMenu().findItem(R.id.companies).setChecked(true);
    }

    public static void setCheckz(){
        mBinding.bottomNav.getMenu().findItem(R.id.home).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBinding.bottomNav.setVisibility(View.VISIBLE);
        mBinding.bottomNav.getMenu().findItem(R.id.home).setChecked(true);
    }
}