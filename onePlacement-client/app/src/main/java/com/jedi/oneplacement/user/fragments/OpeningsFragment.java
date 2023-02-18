package com.jedi.oneplacement.user.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.databinding.FragmentOpeningsBinding;
import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.user.utils.CompanyAdapter;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.DataPersistence;
import com.jedi.oneplacement.utils.UserInstance;

public class OpeningsFragment extends Fragment {
    private static final String TAG = "OpeningsFragment";
    FragmentOpeningsBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentOpeningsBinding.inflate(inflater, container, false);

        mBinding.companyRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.swipeContainer.setOnRefreshListener(() -> fetchTimelineAsync());

        mBinding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return mBinding.getRoot();
    }

    public void fetchTimelineAsync() {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        AdapterFactory.fetchCompanies(requireContext(), companiesList -> {
            DataPersistence.companyList = companiesList;
            mBinding.swipeContainer.setRefreshing(false);
            setAdapt();
        });
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
        super.onResume();
        setAdapt();
    }

    private void setAdapt() {
        AdapterFactory.fetchCompanyAdapter(this, companyAdapter -> {
            mBinding.companyRv.setAdapter(companyAdapter);
            companyAdapter.notifyDataSetChanged();
        });
    }
}