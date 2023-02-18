package com.jedi.oneplacement.user.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.user.fragments.RegisteredCompaniesFragment;

import java.util.ArrayList;

public class RegCompanyAdapter extends RecyclerView.Adapter<RegCompanyAdapter.CompanyViewHolder> {
    private static final String TAG = "CompanyAdapter";
    public ArrayList<Company> companyList = new ArrayList<>();
    private static RegisteredCompaniesFragment registeredCompaniesFragment;

    public RegCompanyAdapter(RegisteredCompaniesFragment registeredCompaniesFragment) {
        RegCompanyAdapter.registeredCompaniesFragment = registeredCompaniesFragment;
    }

    @NonNull
    @Override
    public RegCompanyAdapter.CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_rv_layout, parent, false);
        return new RegCompanyAdapter.CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        holder.bindView(companyList.get(holder.getAdapterPosition()));
    }


    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mCompany;
        TextView ctc, ppo, stipend, cName;
        static MaterialButton mRegBtn;
        RelativeLayout hidden;
        boolean setV = true;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            init();
        }

        public void bindView(Company company) {
            cName.setText(company.getCname());
            if (company.getPpo().matches(""))
                ppo.setVisibility(View.GONE);
            else
                ppo.setText(company.getPpo());

            if (company.getStipend().matches(""))
                stipend.setVisibility(View.GONE);
            else
                stipend.setText(company.getStipend());

            mRegBtn.setVisibility(View.GONE);

            ctc.setText(company.getCtc());
            mCompany.setOnClickListener(v -> {
                if (setV)
                    hidden.setVisibility(View.VISIBLE);
                else
                    hidden.setVisibility(View.GONE);
                setV = !setV;
            });
        }

        public static void disableBtn() {
            mRegBtn.setEnabled(false);
        }

        private void init() {
            mCompany = itemView.findViewById(R.id.company_card);
            ctc = itemView.findViewById(R.id.ctc);
            stipend = itemView.findViewById(R.id.stipend);
            ppo = itemView.findViewById(R.id.ppo);
            mRegBtn = itemView.findViewById(R.id.register_in_company);
            cName = itemView.findViewById(R.id.c_name_text);
            hidden = itemView.findViewById(R.id.hiddenV);
        }
    }
}
