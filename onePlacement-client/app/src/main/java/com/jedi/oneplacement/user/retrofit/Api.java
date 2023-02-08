package com.jedi.oneplacement.user.retrofit;

import com.jedi.oneplacement.user.payloads.FileResponse;
import com.jedi.oneplacement.user.payloads.JwtAuthResponse;
import com.jedi.oneplacement.user.payloads.User;
import com.jedi.oneplacement.user.payloads.UserDto;
import com.jedi.oneplacement.user.payloads.UserLoginInfo;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    /* ----------- AUTHENTICATION --------------- */
    @POST("/api/v1/auth/check")
    Call<Map<String, Object>> checkUser(@Header(AppConstants.AUTH) String token);

    @POST("/api/v1/auth/login/user")
    Call<JwtAuthResponse> loginUser(@Body UserLoginInfo userLoginInfo);

    @POST("/api/v1/auth/register/user/{role}")
    Call<UserDto> registerUser(@Path (value = "role") String role, @Body User user);

    /* --------- USER DATA EDITING ------------ */
    @Multipart
    @POST("/file/upload/image")
    Call<FileResponse> uploadImage(@Header("Authorization") String token, @Part("userID") Integer uId, @Part MultipartBody.Part img);

    @Multipart
    @POST("/file/upload/resume")
    Call<FileResponse> uploadResume(@Header("Authorization") String token, @Part("userID") Integer uId, @Part MultipartBody.Part resume);

    @PUT("/api/users/{userId}")
    Call<UserDto> updateUserDetails(@Header(AppConstants.AUTH) String token, @Path("userId") Integer uId, @Body UserDto userDto);

    /* --------- DOWNLOAD FILES ------------ */
    @GET("/file/retrieve/image/{userId}")
    Call<FileResponse> getImage(@Path(value = "userId") Integer uId, @Header(AppConstants.AUTH) String token);

    @GET("/file/retrieve/resume/{userId}")
    Call<FileResponse> getResume(@Path(value = "userId") Integer uId, @Header(AppConstants.AUTH) String token);
}