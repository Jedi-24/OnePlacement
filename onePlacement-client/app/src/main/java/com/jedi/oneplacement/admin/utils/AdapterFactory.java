package com.jedi.oneplacement.admin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.jedi.oneplacement.admin.fragments.UserListFragment;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.user.fragments.OpeningsFragment;
import com.jedi.oneplacement.user.fragments.RegisteredCompaniesFragment;
import com.jedi.oneplacement.user.utils.CompanyAdapter;
import com.jedi.oneplacement.user.utils.RegCompanyAdapter;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;
import com.jedi.oneplacement.utils.DataPersistence;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AdapterFactory { // optimized trike se fetch only once:
    private static final String TAG = "AdapterFactory";

    public interface fetchUsersListener {
        void onResponse(List<UserDto> usersList);
    }

    public interface fetchAdaptersListener {
        void onResponse(UsersAdapter internUsersAdapter, UsersAdapter placementUsersAdapter);
    }

    public interface fetchCompaniesListener {
        void onResponse(List<Company> companiesList);
    }

    public interface fetchCompanyAdapterListener {
        void onResponse(CompanyAdapter companyAdapter);
    }

    public interface fetchRegCompanyAdapterListener {
        void onResponse(RegCompanyAdapter regCompanyAdapter);
    }

    private AdapterFactory() {
    }

    public static void fetchCompanyAdapter(OpeningsFragment openingsFragment, fetchCompanyAdapterListener listener) {
        CompanyAdapter companyAdapter = new CompanyAdapter(openingsFragment);
        companyAdapter.companyList = (ArrayList<Company>) DataPersistence.companyList;
//        if (registeredCompaniesFragment != null) {
//            // todo: fix this
//            regCompanyAdapter.companyList.addAll(UserInstance.getUserCompanies());
//            Log.d(TAG, "fetchCompanyAdapter: " + regCompanyAdapter.companyList.size());
////            Log.d(TAG, "fetchCompanyAdapter: " + regCompanyAdapter.companyList.get(0).toString());
//        }
        listener.onResponse(companyAdapter);
    }

    public static void fetchRegCompanyAdapter(RegisteredCompaniesFragment registeredCompaniesFragment, fetchRegCompanyAdapterListener listener) {
        RegCompanyAdapter regCompanyAdapter = new RegCompanyAdapter(registeredCompaniesFragment);

        regCompanyAdapter.companyList.addAll(UserInstance.getUserCompanies());
        listener.onResponse(regCompanyAdapter);
    }


    public static void fetchUsersAdapters(UserListFragment userListFragment, fetchAdaptersListener listener) {
        UsersAdapter internUsersAdapter = new UsersAdapter(userListFragment);
        UsersAdapter placementUsersAdapter = new UsersAdapter(userListFragment);
        for (UserDto userDto : DataPersistence.usersList) {
            if (userDto.getId() == UserInstance.getId()) continue;

            User user = new ModelMapper().map(userDto, User.class);
            Set<RoleDto> roles = user.getRoles();
            for (RoleDto roleDto : roles) {
                if (roleDto.getRole_name().matches("ROLE_Internship")) {
                    internUsersAdapter.usersList.add(user);
                } else if (roleDto.getRole_name().matches("ROLE_Placement")) {
                    placementUsersAdapter.usersList.add(user);
                }
            }
        }
        listener.onResponse(internUsersAdapter, placementUsersAdapter);
    }

    public static void fetchUsers(Context ctx, fetchUsersListener listener) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.getAllUsers(jwt, new ApiImpl.ApiCallListener<List<UserDto>>() {
            @Override
            public void onResponse(List<UserDto> response) {
                Log.d(TAG, "onResponse: haa nhai yhi uh meeehe");
                listener.onResponse(response);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(ctx, code + " Failed to fetch users !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void fetchCompanies(Context ctx, fetchCompaniesListener companiesListener) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.getAllCompanies(jwt, new ApiImpl.ApiCallListener<List<Company>>() {
            @Override
            public void onResponse(List<Company> response) {
                companiesListener.onResponse(response);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(ctx, "Error in fetching Companies !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}