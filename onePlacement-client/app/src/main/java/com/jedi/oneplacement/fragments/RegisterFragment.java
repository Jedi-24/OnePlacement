package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jedi.oneplacement.EntryActivity;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.AuthApiImpl;
import com.jedi.oneplacement.utils.AppConstants;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    AutoCompleteTextView autoCompleteTextView;
    TextInputEditText mName,mRegNo,mEmail,mPassword;
    MaterialButton mRegBtn;
    TextView mLogin;

    private String name,password,email,regNo,role;

    EntryActivity mEntryActivity;

    public RegisterFragment(EntryActivity entryActivity) {
        this.mEntryActivity = entryActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);

        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            role = autoCompleteTextView.getText().toString();
            Toast.makeText(requireContext(), autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        mRegBtn.setOnClickListener(v->{
            name = mName.getText().toString();
            password = mPassword.getText().toString();
            email = mEmail.getText().toString();
            regNo = mRegNo.getText().toString();
            mName.setText("");mPassword.setText("");mRegNo.setText("");mEmail.setText("");
            callRegisterApi(name,regNo,email,password,role);
        });

        mLogin.setOnClickListener(v -> mEntryActivity.loadLoginFragment());
        return view;
    }

    private void callRegisterApi(String name, String regNo, String email, String password, String role) {

        AuthApiImpl.registerUser(name, regNo, email, password, role, new AuthApiImpl.ApiCallListener<UserDto>() {
            @Override
            public void onResponse(UserDto response) {
                String jwtToken = response.getJwtToken();
                if(!(jwtToken.isEmpty())){
                    // store token in shared preferences:
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(AppConstants.JWT, jwtToken).apply();

                    startActivity(new Intent(requireContext(), EntryActivity.class));
                }
            }

            @Override
            public void onFailure(int code) {

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
        role = AppConstants.DEFAULT_ROLE;
    }
}