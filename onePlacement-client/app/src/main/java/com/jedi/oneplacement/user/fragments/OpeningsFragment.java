package com.jedi.oneplacement.user.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.data.Repository;
import com.jedi.oneplacement.databinding.FragmentOpeningsBinding;
import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.user.utils.CompanyAdapter;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.data.UserInstance;

import java.util.List;

public class OpeningsFragment extends Fragment {
    private static final String TAG = "OpeningsFragment";
    FragmentOpeningsBinding mBinding;
    int pgN = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentOpeningsBinding.inflate(inflater, container, false);
        mBinding.companyRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        // Fragment Result API to get data using Navigation Component:
        requireActivity().getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            String result = bundle.getString("bundleKey");
            if (result != null && result.matches(AppConstants.PING_SERVER)) fetchTimelineAsync();
        });

        mBinding.nxtPg.setOnClickListener(v -> {
            pgN++;
            if (pgN > 0)
                mBinding.prevPg.setEnabled(true);
            Log.d(TAG, "onCreateView: " + pgN);
            setAdapt(pgN);
        });

        mBinding.prevPg.setOnClickListener(V -> {
            if (pgN > 0) {
                pgN--;
                setAdapt(pgN);
            } else mBinding.prevPg.setEnabled(false);
        });

        mBinding.swipeContainer.setOnRefreshListener(this::fetchTimelineAsync);
        mBinding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        return mBinding.getRoot();
    }

    public void fetchTimelineAsync() {
        pgN = 0;
        Repository.getRepoInstance().fetchCompanies(pgN, requireContext(), new Repository.ResourceListener<List<Company>>() {
            @Override
            public void onSuccess(List<Company> data) {
                mBinding.swipeContainer.setRefreshing(false);
                setAdapt(pgN);
            }

            @Override
            public void onFailure(String errMsg) {

            }
        }, true);
    }

    public void registerInCompany(Company company) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.registerInC(company, token, new ApiImpl.ApiCallListener<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse response) {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                CompanyAdapter.CompanyViewHolder.disableBtn();
                // update UserInstance: todo: ask doubt: and 2 different adapters for registered and openings ?
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(AppConstants.JWT, null);

                // todo: ask doubt on this implementation ?
                UserInstance.fetchUser(token, new UserInstance.FetchListener() {
                    @Override
                    public void onFetch() {
                        Toast.makeText(requireContext(), "Swipe to checkout Registered Companies !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code) {
                        Toast.makeText(requireContext(), code + " :ERR", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(requireContext(), "ERR: " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: " + "resumed again!");
        super.onResume();
        setAdapt(pgN);
    }

    public void setAdapt(int pgN) {
        AdapterFactory.fetchCompanyAdapter(pgN, this, companyAdapter -> {
            mBinding.companyRv.setAdapter(companyAdapter);
            companyAdapter.notifyDataSetChanged();
        });
    }
}