package com.jedi.oneplacement.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.retrofit.ApiImpl;

import java.util.Map;
import java.util.Set;

/**
 * // Navigation graph
 * <p>
 * Launcher Activity -> (/check = jwt + user)
 * -- UserInstance user;
 * `- check() -> res
 * <p>
 * 1) [] -> LoginFragment(user, activity) -> (/login -> user,pass -> jwtToken)
 * -            `- jwtToken [valid] -> user.setJwtToken(token, onFetch {
 * -                // switch to main fragment
 * -                activity.theFunThatSwitches()
 * -            }, on error = {handle UI});
 * <p>
 * 2) {jwt, userPayload} -> user.setJwtToken(token)
 * -                    -> user.setUser(userPayload)
 * -                    start main fragment
 * MainFragment(user, activity) {
 * user.getData();
 * <p>
 * // logout
 * activity.theFunThatSwitchesToLoginFrag()
 * }
 */

public class UserInstance {
    private static final String TAG = "UserInstance";
    private static User mUser = new User();

    public interface FetchListener {
        void onFetch();
        void onError(int code);
    }

    public static void updateJwtToken(String token, FetchListener listener) {
        Log.d(TAG, "updateJwtToken: " + token);
//        mUser.setJwtToken(token);
        fetchUser(token,listener);
    }

    public static void sessionOver(Context context, Fragment fragment){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AppConstants.JWT, "Jedi_24").apply();

        Cache.removeImgFromCache(context);
        Cache.removeResumeFromCache(context);

        NavOptions.Builder navBuilder = new NavOptions.Builder();
        NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigate(R.id.loginFragment, null, navOptions);
    }

    public static void fetchUser(String token, FetchListener listener) {
        // API.someFun(jwtToken, onResponse = {res -> mUser.setWhatever field() } )
        mUser.setJwtToken(token);
        ApiImpl.checkUser(mUser.getJwtToken(), new ApiImpl.ApiCallListener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                Log.d(TAG, "onResponse: " + response);
                String toJson = new Gson().toJson(response.get("Authenticated: "));
                mUser = new Gson().fromJson(toJson, User.class);
                Log.d(TAG, "onResponse: " + mUser.toString());
                listener.onFetch();
            }

            @Override
            public void onFailure(int code) {
                listener.onError(code);
            }
        });
    }

    public static User getUserInstance() {
        return mUser;
    }

    public static void setUserInstance(User user) {
        mUser = user;
        Log.d(TAG, "setUserInstance: " + mUser.toString());
    }

    public static Integer getId() {
        return mUser.getId();
    }

    public static String getName() {
        return mUser.getName();
    }

    public static String getRegNo() {
        return mUser.getRegNo();
    }

    public static String getBranch() {
        return mUser.getBranch();
    }

    public static String getEmail() {
        return mUser.getEmail();
    }

    public static String getCreditPts() {
        return mUser.getTpoCredits();
    }

    public static String getRoleStatus() {
        return mUser.getRoleStatus();
    }

    public static String getProfileStatus() {
        return mUser.getProfileStatus();
    }

    public static Set<RoleDto> getRoles(){
        return mUser.getRoles();
    }
//    public static RoleDto getRole() {
//        return mUser.getRole();
//    }
    public static String getPhoneNumber() {
        return mUser.getPhoneNumber();
    }

    public static Set<Company> getUserCompanies(){
        return mUser.getCompanies();
    }
}