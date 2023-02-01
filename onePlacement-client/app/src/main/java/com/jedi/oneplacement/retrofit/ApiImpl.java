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

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body().isEmpty()) {
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
        getRetrofitAccessObject().registerUser(role, user)
                .enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if(response.body()!=null){
                            listener.onResponse(response.body());
                        }
                        else
                            listener.onFailure(response.code());
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
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