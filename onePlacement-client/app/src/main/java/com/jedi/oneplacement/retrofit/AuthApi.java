package com.jedi.oneplacement.retrofit;

import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.payloads.UserLoginInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthApi {
    @POST("/api/v1/auth/check")
    Call<Map<String, Object>> checkUser(@Header("Authorization") String token);

    @POST("/api/v1/auth/login")
    Call<JwtAuthResponse> loginUser(@Body UserLoginInfo userLoginInfo);

    @POST("/api/v1/auth/register/user/{role}")
    Call<UserDto> registerUser(@Path (value = "role") String role, @Body User user);
}
