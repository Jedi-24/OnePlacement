package com.jedi.oneplacement.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jedi.oneplacement.LoginActivity;
import com.jedi.oneplacement.R;

public class LoginFragment extends Fragment {

    TextInputEditText mUsername,mPassword;
    MaterialButton mBtn;
    TextView mReg;
    LoginActivity mlgnActivity;

    public LoginFragment(LoginActivity loginActivity) {
        this.mlgnActivity = loginActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);

        mBtn.setOnClickListener(v->{
            Toast.makeText(requireContext(), "Loawdaa", Toast.LENGTH_SHORT).show();
        });

        mReg.setOnClickListener(v -> mlgnActivity.loadRegFragment());

        return view;
    }

    private void initViews(View view) {
        mUsername = view.findViewById(R.id.user_name);
        mPassword = view.findViewById(R.id.password);
        mBtn = view.findViewById(R.id.login_btn);
        mReg = view.findViewById(R.id.reg_txt);
    }
}