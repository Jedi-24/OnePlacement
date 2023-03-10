package com.jedi.oneplacement.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedi.oneplacement.admin.utils.AdapterFactory;
import com.jedi.oneplacement.data.UserInstance;
import com.jedi.oneplacement.databinding.FragmentRegisteredCompaniesBinding;
import com.jedi.oneplacement.payloads.Company;

import java.util.HashSet;
import java.util.Set;

public class RegisteredCompaniesFragment extends Fragment {
    private static final String TAG = "RegisteredCompaniesFrag";
    FragmentRegisteredCompaniesBinding mBinding;
    int pgN = 0;
    int totPages = (UserInstance.getUserCompanies().size() / 8) + ((UserInstance.getUserCompanies().size() % 8 != 0) ? 1 : 0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisteredCompaniesBinding.inflate(inflater, container, false);
        mBinding.registeredCompanyRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        mBinding.nxtPg.setOnClickListener(v -> {
            pgN++;
            if (pgN == totPages - 1)
                mBinding.nxtPg.setEnabled(false);
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
            mBinding.nxtPg.setEnabled(true);
        });

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapt(pgN);
    }

    private void setAdapt(int pgN) {
        AdapterFactory.fetchRegCompanyAdapter(pgN, this, regCompanyAdapter -> {
            mBinding.registeredCompanyRv.setAdapter(regCompanyAdapter);
            regCompanyAdapter.notifyDataSetChanged();
        });
    }
}