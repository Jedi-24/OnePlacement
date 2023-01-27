package com.jedi.oneplacement.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/v1/auth/check")
    Call<Map<String, Object>> checkUser(@Header("Authorization") String token);
}
