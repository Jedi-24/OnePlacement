package com.jedi.oneplacement.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.databinding.FragmentUserListBinding;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.DataPersistence;

import java.util.ArrayList;
import java.util.List;

// gets all users and show them in card view:
public class UserListFragment extends Fragment {
    private static final String TAG = "userListFragment";
    FragmentUserListBinding mBinding;

    boolean internCardExpanded, placementCardExpanded;
    private static final List<UserDto> AllS = DataPersistence.usersList;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserListBinding.inflate(inflater, container, false);
        internCardExpanded = false;
        placementCardExpanded = false;

        reset(this);
        mBinding.internUsers.setOnClickListener(v -> {
            toggleExpand(!internCardExpanded, AppConstants.DEFAULT_ROLE);
            internCardExpanded = !internCardExpanded;
        });

        mBinding.placementUsers.setOnClickListener(v -> {
            toggleExpand(!placementCardExpanded, "K");
            placementCardExpanded = !placementCardExpanded;
        });

        mBinding.placementUsersListRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.internUsersListRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        mBinding.swipeContainer.setOnRefreshListener(() -> {
            fetchTimelineAsync();
        });

        return mBinding.getRoot();
    }

    public void fetchTimelineAsync() {
        AdapterFactory.fetchUsers(requireContext(), usersList -> {
            DataPersistence.usersList = usersList;
            mBinding.swipeContainer.setRefreshing(false);
            setAdapt();
        });
    }

    public void loadUserFragment(String jsonData) {
        Bundle result = new Bundle();
        result.putString("bundleKey", jsonData);
        requireActivity().getSupportFragmentManager().setFragmentResult("requestKey", result);
        // to open user specific fragment:
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.userFragment);
    }

    private void toggleExpand(boolean b, String role) {
        if (b && role.matches(AppConstants.DEFAULT_ROLE)) {
            mBinding.internUsersListRv.setVisibility(View.VISIBLE);
            mBinding.iArrowDown.setVisibility(View.INVISIBLE);
            mBinding.iArrowUp.setVisibility(View.VISIBLE);
        } else if (!b && role.matches(AppConstants.DEFAULT_ROLE)) {
            mBinding.internUsersListRv.setVisibility(View.GONE);
            mBinding.iArrowDown.setVisibility(View.VISIBLE);
            mBinding.iArrowUp.setVisibility(View.INVISIBLE);
        } else if (b && !role.matches(AppConstants.DEFAULT_ROLE)) {
            mBinding.placementUsersListRv.setVisibility(View.VISIBLE);
            mBinding.pArrowDown.setVisibility(View.INVISIBLE);
            mBinding.pArrowUp.setVisibility(View.VISIBLE);
        } else {
            mBinding.placementUsersListRv.setVisibility(View.GONE);
            mBinding.pArrowDown.setVisibility(View.VISIBLE);
            mBinding.pArrowUp.setVisibility(View.INVISIBLE);
        }
    }

    private void setAdapt() {
        AdapterFactory.fetchUsersAdapters(UserListFragment.this, (internUsersAdapter, placementUsersAdapter) -> {
            mBinding.placementUsersListRv.setAdapter(placementUsersAdapter);
            placementUsersAdapter.notifyDataSetChanged();
            mBinding.internUsersListRv.setAdapter(internUsersAdapter);
            internUsersAdapter.notifyDataSetChanged();
        });
    }

    public static void searcher(UserListFragment fragment, String query){

        List<UserDto> filteredUsers = new ArrayList<>();

        for(UserDto user: AllS){
            String name = user.getName();
            if(name.contains(query)){
                Log.d(TAG, "searcher: hmmmm");
                filteredUsers.add(user);
            }
        }

        if(filteredUsers.size() == 0){
            Toast.makeText(fragment.requireContext(), "No User found !", Toast.LENGTH_SHORT).show();
            return;
        }

        DataPersistence.usersList = filteredUsers;
        fragment.setAdapt();
    }

    public static void reset(UserListFragment fragment){
        DataPersistence.usersList = AllS;
        fragment.setAdapt();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: " + "hereee");
        super.onResume();
        setAdapt();
    }
}