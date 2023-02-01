package com.jedi.oneplacement.retrofit;

import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.payloads.UserLoginInfo;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    /* ----------- AUTHENTICATION --------------- */
    @POST("/api/v1/auth/check")
    Call<Map<String, Object>> checkUser(@Header(AppConstants.AUTH) String token);

    @POST("/api/v1/auth/login")
    Call<JwtAuthResponse> loginUser(@Body UserLoginInfo userLoginInfo);

    @POST("/api/v1/auth/register/user/{role}")
    Call<UserDto> registerUser(@Path (value = "role") String role, @Body User user);

    /* --------- USER DATA EDITING ------------ */
//    @POST("/file/upload/image")
//    Call<FileResponse> uploadImage(@Header("Authorization") String token, @Query("userID") Integer uId, @Query("image"));

    /* --------- DOWNLOAD FILES ------------ */
    @GET("/file/retrieve/image/{userId}")
    Call<FileResponse> getImage(@Path(value = "userId") Integer uId, @Header(AppConstants.AUTH) String token); // fileInputstream ?
}
