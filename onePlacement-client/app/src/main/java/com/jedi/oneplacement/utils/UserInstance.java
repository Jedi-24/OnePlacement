package com.jedi.oneplacement.utils;

import com.google.gson.Gson;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.retrofit.AuthApiImpl;

import java.util.Map;

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
    private static User mUser = new User();

    public interface FetchListener {
        void onFetch();

        void onError(int code);
    }

    public static void updateJwtToken(String token, FetchListener listener) {
        mUser.setJwtToken(token);
        fetchUser(listener);
    }

    private static void fetchUser(FetchListener listener) {
        // API.someFun(jwtToken, onResponse = {res -> mUser.setWhatever field() } )
        AuthApiImpl.checkUser(mUser.getJwtToken(), new AuthApiImpl.ApiCallListener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                String toJson = new Gson().toJson(response.get("Authenticated: "));
                mUser = new Gson().fromJson(toJson, User.class);
                listener.onFetch();
            }

            @Override
            public void onFailure(int code) {
                listener.onError(code);
            }
        });
    }

    public static String getName() {
        return mUser.getName();
    }

    public static String getJwtToken() {
        return mUser.getJwtToken();
    }
}