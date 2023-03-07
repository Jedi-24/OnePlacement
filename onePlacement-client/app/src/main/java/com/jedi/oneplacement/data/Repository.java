package com.jedi.oneplacement.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.Api;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

// this class deals with all the data, whatever fragment / activity needs some data, it is provided by this class only:
public class Repository {
    // TODO : singleton class: to use a single instance everywhere
    private static Repository instance = null;

    private Repository() {
    }

    public static Repository getRepoInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public interface ResourceListener<T> {
        void onSuccess(T data);

        void onFailure(String errMsg);
    }

    private static List<User> randomList;
    private static List<UserDto> usersList = new ArrayList<>();
    private static List<Company> companyList = new ArrayList<>();

    // setters for lists: for search feature:
    public void setUsersList(List<UserDto> filteredUsers){
        usersList = filteredUsers;
    }
    // not used abhi:
    public void setCompanyList(List<Company> filteredCompanies){
        companyList = filteredCompanies;
    }

    public void fetchCompanies(Context ctx, ResourceListener<List<Company>> companiesListener, boolean forceFetch) {
        if (!companyList.isEmpty() || !forceFetch) {
            companiesListener.onSuccess(companyList);
            return;
        }

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.getAllCompanies(jwt, new ApiImpl.ApiCallListener<List<Company>>() {
            @Override
            public void onResponse(List<Company> response) {
                companyList = response;
                companiesListener.onSuccess(response);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(ctx, "Error in fetching Companies !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchUsers(Context ctx, ResourceListener<List<UserDto>> listener, boolean forceFetch) {
        if (!usersList.isEmpty() || !forceFetch) {
            listener.onSuccess(usersList);
            return;
        }

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.getAllUsers(jwt, new ApiImpl.ApiCallListener<List<UserDto>>() {
            @Override
            public void onResponse(List<UserDto> response) {
                usersList = response;
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(ctx, code + " Failed to fetch users !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // sample function definition:
    public void fun(ResourceListener<List<User>> listener, boolean forceFetch) {
        if (!randomList.isEmpty() || !forceFetch) {
            listener.onSuccess(randomList);
            return;
        }
        // Api Calls here:
    }
}
