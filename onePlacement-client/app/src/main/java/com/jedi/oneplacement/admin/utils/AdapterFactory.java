package com.jedi.oneplacement.admin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.jedi.oneplacement.admin.fragments.UserListFragment;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.user.payloads.RoleDto;
import com.jedi.oneplacement.user.payloads.User;
import com.jedi.oneplacement.user.payloads.UserDto;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.UserInstance;
import com.jedi.oneplacement.utils.UsersDataPersistance;

import org.modelmapper.ModelMapper;

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

    private AdapterFactory(){
    }
    public static void fetchAdapters(UserListFragment userListFragment, fetchAdaptersListener listener){
        UsersAdapter internUsersAdapter = new UsersAdapter(userListFragment);
        UsersAdapter placementUsersAdapter = new UsersAdapter(userListFragment);
        for (UserDto userDto : UsersDataPersistance.usersList) {
            if(userDto.getId() == UserInstance.getId()) continue;

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
        // get data from backend and update usersList and notifyDataSetChanged
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
}