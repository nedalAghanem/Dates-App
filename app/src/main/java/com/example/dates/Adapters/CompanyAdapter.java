package com.example.dates.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dates.Models.CompanyModel;
import com.example.dates.R;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.MyViewHolder> {

    private ArrayList<CompanyModel> albumModels;
    private CompanyModel companyModel = new CompanyModel();

    class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editName;
        EditText editAddress;
        EditText editTime;
        EditText editPhone;

        MyViewHolder(View view) {
            super(view);
            editName = view.findViewById(R.id.customCompanyName);
            editAddress = view.findViewById(R.id.customCompanyAddress);
            editTime = view.findViewById(R.id.customTimeWork);
            editPhone = view.findViewById(R.id.customContact);

        }

    }

    public CompanyAdapter(ArrayList<CompanyModel> albumModels) {
        this.albumModels = albumModels;
    }

    public CompanyAdapter() {
    }

    @NonNull
    @Override
    public CompanyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_company, parent, false);

        return new CompanyAdapter.MyViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CompanyAdapter.MyViewHolder holder,
                                 final int position) {

        companyModel = albumModels.get(position);
        if (companyModel != null) {
            holder.editName.setText(companyModel.getName());
            holder.editAddress.setText(companyModel.getLat() + companyModel.getLng());
            holder.editTime.setText(companyModel.getStart_time() + " - " + companyModel.getEnd_time());
            holder.editPhone.setText(companyModel.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return albumModels.size();
    }

}
