package com.jedi.oneplacement.retrofit;

import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.LoginInfo;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/v1/auth/check")
    Call<Map<String, Object>> checkUser(@Header("Authorization") String token);

    @POST("/api/v1/auth/login")
    Call<JwtAuthResponse> loginUser(@Body LoginInfo loginInfo);
}
