package com.jedi.oneplacement.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.payloads.UserLoginInfo;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

import org.modelmapper.ModelMapper;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiImpl {
    private static final String TAG = "AuthApiImpl";
    private static Api mApi = null;

    private static Api getRetrofitAccessObject() {
        if (mApi == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mApi = retrofit.create(Api.class);
        }
        return mApi;
    }

    public interface ApiCallListener<T> {
        void onResponse(T response);
        void onFailure(int code);
    }

    public static void loginUser(String userName, String password, ApiCallListener<JwtAuthResponse> listener) {
        getRetrofitAccessObject().loginUser(new UserLoginInfo(userName, password)).enqueue(new Callback<JwtAuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtAuthResponse> call, @NonNull Response<JwtAuthResponse> response) {
                JwtAuthResponse jwt = response.body();
                if (!response.isSuccessful() || jwt == null) {
                    listener.onFailure(response.code());
                    return;
                }
                listener.onResponse(jwt);
            }

            @Override
            public void onFailure(Call<JwtAuthResponse> call, Throwable t) {
                listener.onFailure(-1);
            }
        });
    }

    public static void checkUser(String token, ApiCallListener<Map<String, Object>> listener) {
        getRetrofitAccessObject().checkUser("Bearer " + token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                if (!response.isSuccessful() || response.body().isEmpty()) {
                    listener.onFailure(response.code());
                } else {
                    listener.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                listener.onFailure(-1);
            }
        });
    }

    public static void registerUser(String name, String regNo, String email, String password, String role, ApiCallListener<UserDto> listener){
        User user = new User();
        user.setName(name);
        user.setRegNo(regNo);
        user.setEmail(email);
        user.setPassword(password);
        user.setTpoCredits("10");
        user.setRoleStatus(AppConstants.NOT_OFFERED);
        user.setProfileStatus(AppConstants.NOT_VERIFIED);

        getRetrofitAccessObject().registerUser(role, user)
                .enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void uploadImg(String token, MultipartBody.Part img , ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().uploadImage("Bearer " + token, UserInstance.getId(), img)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(!response.isSuccessful() || response.body()==null) {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void uploadResume(String token, MultipartBody.Part resume , ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().uploadResume("Bearer " + token, UserInstance.getId(), resume)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(!response.isSuccessful() || response.body()==null) {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }

    public static void updateUser(String token, String branch, String email, String mno, ApiCallListener<UserDto> listener){
        UserDto userDto = new ModelMapper().map(UserInstance.getUserInstance(), UserDto.class);
        userDto.setBranch(branch);
        userDto.setEmail(email);
        userDto.setPhoneNumber(mno);

        Log.d(TAG, "updateUser: " + userDto.toString());

        getRetrofitAccessObject().updateUserDetails("Bearer " + token, UserInstance.getId(), userDto)
                .enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                        if(!response.isSuccessful() || response.body()==null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void getResume(Integer uid, String token, ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().getResume(uid, "Bearer " + token)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }

    public static void getImage(Integer uid,String token,ApiCallListener<FileResponse> listener){
        Log.d(TAG, "getImage: " + token);

        getRetrofitAccessObject().getImage(uid,"Bearer " + token)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                        if(response.body()==null) {
                            listener.onFailure(response.code());
                            Log.d(TAG, "onFailure: hehehehe");
                            return;
                        }

                        Log.d(TAG, "onResponse: " + response.body());
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {

                        Log.d(TAG, "onFailure: " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }
}