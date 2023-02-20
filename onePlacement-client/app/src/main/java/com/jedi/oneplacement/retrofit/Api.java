package com.jedi.oneplacement.retrofit;

import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.NotifMessage;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.payloads.UserLoginInfo;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.List;
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
    Call<FileResponse> uploadImage(@Header(AppConstants.AUTH) String token, @Part("userID") Integer uId, @Part MultipartBody.Part img);

    @POST("/api/users/setup/devToken/{token}/{userId}")
    Call<ApiResponse> setupDevToken(@Header(AppConstants.AUTH) String token, @Path("token") String devToken, @Path("userId") Integer uId);

    @Multipart
    @POST("/file/upload/resume")
    Call<FileResponse> uploadResume(@Header(AppConstants.AUTH) String token, @Part("userID") Integer uId, @Part MultipartBody.Part resume);

    @PUT("/api/users/{userId}")
    Call<UserDto> updateUserDetails(@Header(AppConstants.AUTH) String token, @Path("userId") Integer uId, @Body UserDto userDto);

    @PUT("/api/users/credits/{userId}/{credits}")
    Call<ApiResponse> setCredits(@Header(AppConstants.AUTH) String token, @Path("userId") Integer uId, @Path("credits") int credits);

    @PUT("/api/users/verify/{userId}")
    Call<ApiResponse> verifyProfile(@Header(AppConstants.AUTH) String token, @Path("userId") Integer uId);

    /* --------- GET ALL USERS ------------- */
    @GET("/api/users/")
    Call<List<UserDto>> getAllUsers(@Header(AppConstants.AUTH) String token);
    @GET("/api/users/search/{query}")
    Call<List<UserDto>> searchUsers(@Header(AppConstants.AUTH) String token, @Path("query") String query);

    /* --------- DOWNLOAD FILES ------------ */
    @GET("/file/retrieve/image/{userId}")
    Call<FileResponse> getImage(@Path(value = "userId") Integer uId, @Header(AppConstants.AUTH) String token);

    @GET("/file/retrieve/resume/{userId}")
    Call<FileResponse> getResume(@Path(value = "userId") Integer uId, @Header(AppConstants.AUTH) String token);

    /* -------- COMPANY CALLS -------- */
    @POST("/company/save/{role}")
    Call<Company> addCompany(@Path(value = "role") String role, @Body Company company, @Header(AppConstants.AUTH) String token);
    @GET("/company/{role}")
    Call<List<Company>> fetchCompanies(@Path(value = "role") String role, @Header(AppConstants.AUTH) String token);
    @POST("/api/users/company/register/{userId}")
    Call<ApiResponse> registerInC(@Body Company company, @Header(AppConstants.AUTH) String token, @Path(value = "userId") Integer uId);

    @POST("/notification/{Role}")
    Call<ApiResponse> sendNotification(@Header(AppConstants.AUTH) String token, @Path(value = "Role") String role, @Body NotifMessage msg);
}