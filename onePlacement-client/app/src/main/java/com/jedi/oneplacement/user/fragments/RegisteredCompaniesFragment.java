package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.databinding.FragmentRegisteredCompaniesBinding;
import com.jedi.oneplacement.payloads.Company;

import java.util.HashSet;
import java.util.Set;

public class RegisteredCompaniesFragment extends Fragment {
    private static final String TAG = "RegisteredCompaniesFrag";
    FragmentRegisteredCompaniesBinding mBinding;
    Set<Company> regC = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisteredCompaniesBinding.inflate(inflater, container, false);
        mBinding.registeredCompanyRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

//        regC = UserInstance.getUserCompanies();
//        Toast.makeText(requireContext(), regC.iterator().next().getCname(), Toast.LENGTH_SHORT).show();

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        AdapterFactory.fetchRegCompanyAdapter(this, regCompanyAdapter -> {
            mBinding.registeredCompanyRv.setAdapter(regCompanyAdapter);
            regCompanyAdapter.notifyDataSetChanged();
        });
    }
}