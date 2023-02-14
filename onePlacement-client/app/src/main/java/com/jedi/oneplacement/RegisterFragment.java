package com.jedi.oneplacement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jedi.oneplacement.activities.MainActivity;
import com.jedi.oneplacement.databinding.FragmentRegisterBinding;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

public class RegisterFragment extends Fragment {
    private String name, password, email, regNo, role;
    FragmentRegisterBinding mBinding = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        mBinding.layout.title.setVisibility(View.VISIBLE);
        mBinding.layout.title.setText(AppConstants.APP);
        mBinding.layout.userPhoto.setImageResource(R.mipmap.ic_launcher_foreground);

        role = AppConstants.DEFAULT_ROLE;

        mBinding.autoCompleteTextview.setOnItemClickListener((parent, view1, position, id) -> {
            role = mBinding.autoCompleteTextview.getText().toString();
            Toast.makeText(requireContext(), mBinding.autoCompleteTextview.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        mBinding.regBtn.setOnClickListener(v -> {
            name = mBinding.nameInput.getText().toString();
            password = mBinding.regPasswordInput.getText().toString();
            email = mBinding.emailInput.getText().toString();
            regNo = mBinding.regNoInput.getText().toString();
            mBinding.nameInput.setText("");
            mBinding.regPasswordInput.setText("");
            mBinding.emailInput.setText("");
            mBinding.regNoInput.setText("");
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
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
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