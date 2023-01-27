package com.jedi.oneplacement; // I am going to give it my ALL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.jedi.oneplacement.retrofit.AuthApi;
import com.jedi.oneplacement.retrofit.RetrofitInitializer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MaterialButton mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogout = findViewById(R.id.log_out_btn);

        mLogout.setOnClickListener(view->{
            SharedPreferences sharedPreferences = this.getSharedPreferences("ONE_PLACEMENT", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("JWT", "Jedi_24").apply();
            startLoginWindow();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserSession();
    }

    private void checkUserSession() {
        RetrofitInitializer retrofitInitializer = new RetrofitInitializer();
        AuthApi auth = retrofitInitializer.getRetrofit().create(AuthApi.class);

        // retrieve token from shared preferences:
        SharedPreferences sharedPreferences = this.getSharedPreferences("ONE_PLACEMENT", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT", null);

        auth.checkUser("Bearer " + token)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if(response.body().isEmpty()){
                            startLoginWindow();
                        }
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