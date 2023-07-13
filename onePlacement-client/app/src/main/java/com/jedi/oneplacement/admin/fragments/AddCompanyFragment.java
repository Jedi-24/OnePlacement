package com.jedi.oneplacement.admin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentAddCompanyBinding;
import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.NotifMessage;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;

public class AddCompanyFragment extends Fragment {
    private static final String TAG = "AddCompanyFragment";
    FragmentAddCompanyBinding mBinding;

    private String cName, mProfile, mStipend, mCtc, mRole, mPPO;

    public AddCompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddCompanyBinding.inflate(inflater, container, false);
        mRole = AppConstants.DEFAULT_ROLE;
        mPPO = "Yes";

        mBinding.autoCompleteTextviewPPO.setOnItemClickListener((parent, view, position, id) -> {
            mPPO = mBinding.autoCompleteTextview.getText().toString();
        });

        mBinding.autoCompleteTextview.setOnItemClickListener((parent, view, position, id) -> {
            mRole = mBinding.autoCompleteTextview.getText().toString();
            if (id == 1) {
                mBinding.stipend.setVisibility(View.GONE);
                mBinding.ppo.setVisibility(View.GONE);
                mBinding.stipendInput.setText("");
                mPPO = "";
            } else {
                mBinding.stipend.setVisibility(View.VISIBLE);
                mBinding.ppo.setVisibility(View.VISIBLE);
                mPPO = "Yes";
            }
        });

        mBinding.buttonAddC.setOnClickListener(v -> {
            cName = mBinding.cNameInput.getText().toString();
            mProfile = mBinding.profileInput.getText().toString();
            mStipend = mBinding.stipendInput.getText().toString();
            mCtc = mBinding.ctcInput.getText().toString();

            mBinding.cNameInput.setText("");
            mBinding.profileInput.setText("");
            mBinding.stipendInput.setText("");
            mBinding.ctcInput.setText("");

            // send request to add a company:
            callToAddCompany(cName, mProfile, mStipend, mCtc, mPPO, mRole);
        });

        return mBinding.getRoot();
    }

    private void callToAddCompany(String cName, String mProfile, String mStipend, String mCtc, String mPPO, String mRole) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.addCompany(jwt, cName, mProfile, mStipend, mCtc, mPPO, mRole,new ApiImpl.ApiCallListener<Company>() {
            @Override
            public void onResponse(Company response) {
                Log.d(TAG, "onResponse: " + response.toString());
                Toast.makeText(requireContext(), "success !", Toast.LENGTH_SHORT).show();
                // reflect in all companies:
                // send notification:
                NotifMessage notifMessage = new NotifMessage();
                notifMessage.setTitle(cName);
                notifMessage.setBody("is visiting our Campus, Check it out it's Details !");
                ApiImpl.sendNotification(mRole, jwt, notifMessage, new ApiImpl.ApiCallListener<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        Log.d(TAG, "onResponse: sending noification");
                        Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(TAG, "onFailure: " + code);
                    }
                });
            }
            @Override
            public void onFailure(int code) {
                if(code == -1)
                    Toast.makeText(requireContext(), "check your internet connection!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), code + " :ERR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] subjects = getResources().getStringArray(R.array.cFor);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, subjects);
        mBinding.autoCompleteTextview.setAdapter(adapter);

        String[] ppoSubjects = getResources().getStringArray(R.array.PPO);
        ArrayAdapter<String> adapterPPO = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, ppoSubjects);
        mBinding.autoCompleteTextviewPPO.setAdapter(adapterPPO);
    }
}