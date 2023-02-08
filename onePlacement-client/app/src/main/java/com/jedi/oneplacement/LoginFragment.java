package com.jedi.oneplacement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.databinding.FragmentLoginBinding;
import com.jedi.oneplacement.user.payloads.JwtAuthResponse;
import com.jedi.oneplacement.user.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    FragmentLoginBinding mBinding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater,container, false);
        mBinding.loginBtn.setOnClickListener(v -> {
            callLoginApi(mBinding.userName.getText().toString(), mBinding.password.getText().toString());
            mBinding.userName.setText("");
            mBinding.password.setText("");
        });

        mBinding.regTxt.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment));

        return mBinding.getRoot();
    }

    private void callLoginApi(String mUser, String password) {
        ApiImpl.loginUser(mUser, password, new ApiImpl.ApiCallListener<JwtAuthResponse>() {
            @Override
            public void onResponse(JwtAuthResponse response) {
                String token = response.getToken();
                Log.d(TAG, "onResponse: " + token);
                if (!(token.isEmpty())) {
                    // store token in shared preferences:
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(AppConstants.JWT, token).apply();
                    redirectToHome(token);
                }
            }

            @Override
            public void onFailure(int code) {
                if (code == -1) {
                    // no internet
                    Toast.makeText(requireContext(), code + ": Error could not login, check your Internet.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), code + ": Bad Request..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectToHome(String token) {
        UserInstance.updateJwtToken(token, new UserInstance.FetchListener() {
            @Override
            public void onFetch() {
                // check if the user is admin ? -> redirect to adminHomePage.
                if(UserInstance.getRole().getRole_name().matches(AppConstants.ADMIN_ROLE)){
                    NavOptions.Builder navBuilder = new NavOptions.Builder();
                    NavOptions navOptions = navBuilder.setPopUpTo(R.id.loginFragment, true).build();
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.adminHomeFragment, null, navOptions);
                    return;
                }
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_homeFragment);
            }

            @Override
            public void onError(int code) {
                Log.d(TAG, "onError: haa bhai ajajajajja");
                Toast.makeText(requireContext(), "TERI MAA KI...", Toast.LENGTH_SHORT).show();
                // in any case, BAD REQUEST | UNSUCCESSFUL REQUEST :
//                mEntryActivity.loadLoginFragment();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}