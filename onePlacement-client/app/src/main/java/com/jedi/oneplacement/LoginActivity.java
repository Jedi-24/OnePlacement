package com.jedi.oneplacement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jedi.oneplacement.fragments.LoginFragment;
import com.jedi.oneplacement.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {
    LoginFragment loginFragment;
    RegisterFragment registerFragment;

    TextView mRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        loginFragment = new LoginFragment(this);
        registerFragment = new RegisterFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, loginFragment).commit();
    }

    public void loadRegFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, registerFragment).commit();
    }
}
