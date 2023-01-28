package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jedi.oneplacement.LoginActivity;
import com.jedi.oneplacement.MainActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.AuthApi;
import com.jedi.oneplacement.retrofit.RetrofitInitializer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    AutoCompleteTextView autoCompleteTextView;
    TextInputEditText mName,mRegNo,mEmail,mPassword;
    MaterialButton mRegBtn;
    TextView mLogin;

    private String name,password,email,regNo,role;

    LoginActivity mLoginActivity;

    public RegisterFragment(LoginActivity loginActivity) {
        this.mLoginActivity = loginActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                role = autoCompleteTextView.getText().toString();
                Toast.makeText(requireContext(), autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mRegBtn.setOnClickListener(v->{
            name = mName.getText().toString();
            password = mPassword.getText().toString();
            email = mEmail.getText().toString();
            regNo = mRegNo.getText().toString();
            mName.setText("");mPassword.setText("");mRegNo.setText("");mEmail.setText("");
            callRegisterApi(name,regNo,email,password,role);
        });

        mLogin.setOnClickListener(v -> mLoginActivity.loadLoginFragment());
        return view;
    }

    private void callRegisterApi(String name, String regNo, String email, String password, String role) {
        RetrofitInitializer retrofitInitializer = new RetrofitInitializer();
        AuthApi auth = retrofitInitializer.getRetrofit().create(AuthApi.class);

        User user = new User();
        user.setName(name);
        user.setRegNo(regNo);
        user.setEmail(email);
        user.setPassword(password);
        Log.d(TAG, "callRegisterApi: " + user.toString());

        auth.registerUser(role, user)
                .enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if(response.body()!=null){
                            String jwtToken = response.body().getJwtToken();
                            if(!(jwtToken.isEmpty())){
                                // store token in shared preferences:
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ONE_PLACEMENT", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("JWT", jwtToken).apply();

                                startActivity(new Intent(requireContext(), MainActivity.class));
                            }
                        }
                        else{
                            Toast.makeText(mLoginActivity, "Something's Wrong, try again later !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] subjects = getResources().getStringArray(R.array.options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item,subjects);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void initViews(View view) {
        autoCompleteTextView = view.findViewById(R.id.AutoCompleteTextview);

        mName = view.findViewById(R.id.name);
        mRegNo = view.findViewById(R.id.reg_no);
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.reg_password);

        mRegBtn = view.findViewById(R.id.reg_btn);
        mLogin = view.findViewById(R.id.login_txt);
        role = "Internship";
    }
}