package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.databinding.FragmentOpeningsBinding;
import com.jedi.oneplacement.user.utils.CompanyAdapter;

public class OpeningsFragment extends Fragment {
    FragmentOpeningsBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentOpeningsBinding.inflate(inflater,container,false);

        mBinding.companyRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        AdapterFactory.fetchCompanyAdapter(companyAdapter -> {
            mBinding.companyRv.setAdapter(companyAdapter);
            companyAdapter.notifyDataSetChanged();
        });
        return mBinding.getRoot();
    }
}