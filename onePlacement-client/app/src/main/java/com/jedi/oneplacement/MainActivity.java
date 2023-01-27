package com.jedi.oneplacement; // I am going to give it my ALL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jedi.oneplacement.fragments.LoginFragment;
import com.jedi.oneplacement.fragments.RegisterFragment;
import com.jedi.oneplacement.retrofit.AuthApi;
import com.jedi.oneplacement.retrofit.RetrofitInitializer;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    LoginFragment loginFragment;
    RegisterFragment registerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserSession();
    }

    private void checkUserSession() {
        RetrofitInitializer retrofitInitializer = new RetrofitInitializer();
        AuthApi auth = retrofitInitializer.getRetrofit().create(AuthApi.class);

        auth.checkUser(AppConstants.JWT)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if(response.body().isEmpty()){
                            startLoginWindow();
                        }
                        Log.d(TAG, "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        startLoginWindow();
                    }
                });
    }

    private void startLoginWindow(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}