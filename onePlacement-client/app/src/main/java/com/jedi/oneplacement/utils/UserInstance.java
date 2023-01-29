//package com.jedi.oneplacement.utils;
//
//import com.jedi.oneplacement.payloads.User;
//
///**
// *
// * // Navigation graph
// *
// * Launcher Activity -> (/check = jwt + user)
// * -- UserInstance user;
// * `- check() -> res
// * <p>
// * 1) [] -> LoginFragment(user, activity) -> (/login -> user,pass -> jwtToken)
// * -            `- jwtToken [valid] -> user.setJwtToken(token, onFetch {
// * -                // switch to main fragment
// * -                activity.theFunThatSwitches()
// * -            }, on error = {handle UI});
// * <p>
// * 2) {jwt, userPayload} -> user.setJwtToken(token)
// * -                    -> user.setUser(userPayload)
// * -                    start main fragment
// * MainFragment(user, activity) {
// *     user.getData();
// *
// *     // logout
// *     activity.theFunThatSwitchesToLoginFrag()
// * }
// */
//
//public class UserInstance {
//
//    private volatile static UserInstance sInstance = null;
//
//    private UserInstance() {
//        mUser = new User();
//        mUser.setJwtToken("jedi");
//    }
//
//    private static UserInstance getInstance() {
//        return new UserInstance();
//    }
//
//    private static User mUser;
//
//    public static void setJwtToken(String token) {
//        mUser.setJwtToken(token);
//    }
//
//    public static void setUser(User user) {
//        if (user == null) return;
//        mUser = user;
//    }
//
//    public static void updateJwtToken(String token, FetchListener listener) {
//        mUser.setJwtToken(token);
//        fetchUser(listener);
//    }
//
//    public static void fetchUser(FetchListener listener) {
//        // TODO: /check point
//        // API.someFun(jwtToken, onResponse = {res -> mUser.setWahteverfield() } )
//
//        // on success
//        listener.onFetch();
//
//        // on error
//        listener.onError();
//    }
//
//    public static String getName() {
//        return mUser.getName();
//    }
//
//    public static String getJwtToken() {
//        return mUser.getJwtToken();
//    }
//
//    public interface FetchListener {
//        void onFetch();
//
//        void onError();
//    }
//}