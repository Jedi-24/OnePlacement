package com.jedi.oneplacement.admin.utils;

import com.jedi.oneplacement.admin.fragments.UserListFragment;
import com.jedi.oneplacement.data.Repository;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.user.fragments.OpeningsFragment;
import com.jedi.oneplacement.user.fragments.RegisteredCompaniesFragment;
import com.jedi.oneplacement.user.utils.CompanyAdapter;
import com.jedi.oneplacement.user.utils.RegCompanyAdapter;
import com.jedi.oneplacement.utils.UserInstance;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdapterFactory { // optimized trike se fetch only once:
    private static final String TAG = "AdapterFactory";

    public interface fetchAdaptersListener {
        void onResponse(UsersAdapter internUsersAdapter, UsersAdapter placementUsersAdapter);
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

        Repository.getRepoInstance().fetchCompanies(openingsFragment.requireContext(), new Repository.ResourceListener<List<Company>>() {
            @Override
            public void onSuccess(List<Company> data) {
                companyAdapter.companyList = (ArrayList<Company>) data;
                listener.onResponse(companyAdapter);
            }
            @Override
            public void onFailure(String errMsg) {
            }
        }, false);
//        if (registeredCompaniesFragment != null) {
//            // todo: fix this
//            regCompanyAdapter.companyList.addAll(UserInstance.getUserCompanies());
//            Log.d(TAG, "fetchCompanyAdapter: " + regCompanyAdapter.companyList.size());
////            Log.d(TAG, "fetchCompanyAdapter: " + regCompanyAdapter.companyList.get(0).toString());
//        }
    }

    public static void fetchRegCompanyAdapter(RegisteredCompaniesFragment registeredCompaniesFragment, fetchRegCompanyAdapterListener listener) {
        RegCompanyAdapter regCompanyAdapter = new RegCompanyAdapter(registeredCompaniesFragment);

        regCompanyAdapter.companyList.addAll(UserInstance.getUserCompanies());
        listener.onResponse(regCompanyAdapter);
    }


    public static void fetchUsersAdapters(UserListFragment userListFragment, fetchAdaptersListener listener) {
        UsersAdapter internUsersAdapter = new UsersAdapter(userListFragment);
        UsersAdapter placementUsersAdapter = new UsersAdapter(userListFragment);
        Repository.getRepoInstance().fetchUsers(userListFragment.requireContext(), new Repository.ResourceListener<List<UserDto>>() {
            @Override
            public void onSuccess(List<UserDto> data) {
                for (UserDto userDto : data) {
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
            @Override
            public void onFailure(String errMsg) {
            }
        }, false);
    }
}