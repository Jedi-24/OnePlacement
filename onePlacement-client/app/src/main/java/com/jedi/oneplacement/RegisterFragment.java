package com.jedi.oneplacement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jedi.oneplacement.databinding.FragmentRegisterBinding;
import com.jedi.oneplacement.user.payloads.UserDto;
import com.jedi.oneplacement.user.retrofit.ApiImpl;
import com.jedi.oneplacement.user.utils.AppConstants;
import com.jedi.oneplacement.user.utils.UserInstance;

public class RegisterFragment extends Fragment {
    private String name, password, email, regNo, role;
    FragmentRegisterBinding mBinding = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        role = AppConstants.DEFAULT_ROLE;

        mBinding.autoCompleteTextview.setOnItemClickListener((parent, view1, position, id) -> {
            role = mBinding.autoCompleteTextview.getText().toString();
            Toast.makeText(requireContext(), mBinding.autoCompleteTextview.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        mBinding.regBtn.setOnClickListener(v -> {
            name = mBinding.name.getText().toString();
            password = mBinding.regPassword.getText().toString();
            email = mBinding.email.getText().toString();
            regNo = mBinding.regNo.getText().toString();
            mBinding.name.setText("");
            mBinding.regPassword.setText("");
            mBinding.email.setText("");
            mBinding.regNo.setText("");
            callRegisterApi(name, regNo, email, password, role);
        });

        return mBinding.getRoot();
    }

    private void callRegisterApi(String name, String regNo, String email, String password, String role) {
        ApiImpl.registerUser(name, regNo, email, password, role, new ApiImpl.ApiCallListener<UserDto>() {
            @Override
            public void onResponse(UserDto response) {
                String jwtToken = response.getJwtToken();
                if (!(jwtToken.isEmpty())) {
                    // store token in shared preferences:
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(AppConstants.JWT, jwtToken).apply();
                    redirectToHome(jwtToken);
                }
            }

            @Override
            public void onFailure(int code) {
            }
        });
    }

    private void redirectToHome(String token) {
        UserInstance.updateJwtToken(token, new UserInstance.FetchListener() {
            @Override
            public void onFetch() {
                NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.action_registerFragment_to_homeFragment);
            }

            @Override
            public void onError(int code) {
                Toast.makeText(requireContext(), "Error thrown with code: " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] subjects = getResources().getStringArray(R.array.options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, subjects);
        mBinding.autoCompleteTextview.setAdapter(adapter);
    }
}